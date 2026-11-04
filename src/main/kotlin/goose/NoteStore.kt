package goose

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories

data class Note(val id: Long, val text: String)

interface NoteStore {
    suspend fun all(): List<Note>
    suspend fun add(text: String): Note
    suspend fun remove(id: Long)
}

/** SQLite-fil fra DB_PATH (satt til /data/goose.db i containeren), ellers ./data/goose.db lokalt. */
fun defaultNoteStore(): NoteStore =
    SqliteNoteStore(Path.of(System.getenv("DB_PATH") ?: "data/goose.db"))

/**
 * SQLite via ren JDBC. Én åpen tilkobling, serialisert med en lås – SQLite har uansett
 * bare én skriver om gangen, og for en hobbyapp er dette både enkelt og raskt nok.
 * JDBC-kall er blokkerende, så de kjøres på Dispatchers.IO.
 */
class SqliteNoteStore(dbFile: Path) : NoteStore, AutoCloseable {
    private val connection: Connection

    init {
        dbFile.absolute().parent?.createDirectories()
        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.absolute()}")
        connection.createStatement().use { st ->
            st.execute("PRAGMA journal_mode=WAL")
            st.execute("PRAGMA busy_timeout=5000")
            migrate(st)
        }
    }

    /** Enkle, idempotente migreringer. Legg til nye steg nederst og øk user_version. */
    private fun migrate(st: Statement) {
        val version = st.executeQuery("PRAGMA user_version").use { it.next(); it.getInt(1) }
        if (version < 1) {
            st.execute(
                """
                CREATE TABLE IF NOT EXISTS notes (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    text       TEXT    NOT NULL,
                    created_at TEXT    NOT NULL DEFAULT (datetime('now'))
                )
                """.trimIndent()
            )
            st.execute("PRAGMA user_version = 1")
        }
    }

    private suspend fun <T> db(block: (Connection) -> T): T =
        withContext(Dispatchers.IO) { synchronized(connection) { block(connection) } }

    override suspend fun all(): List<Note> = db { c ->
        c.prepareStatement("SELECT id, text FROM notes ORDER BY id").use { ps ->
            ps.executeQuery().use { rs ->
                buildList { while (rs.next()) add(Note(rs.getLong(1), rs.getString(2))) }
            }
        }
    }

    override suspend fun add(text: String): Note = db { c ->
        c.prepareStatement("INSERT INTO notes(text) VALUES (?) RETURNING id").use { ps ->
            ps.setString(1, text)
            ps.executeQuery().use { rs -> rs.next(); Note(rs.getLong(1), text) }
        }
    }

    override suspend fun remove(id: Long) {
        db { c ->
            c.prepareStatement("DELETE FROM notes WHERE id = ?").use { ps ->
                ps.setLong(1, id)
                ps.executeUpdate()
            }
        }
    }

    override fun close() = connection.close()
}

/** Til tester og rask prototyping. */
class InMemoryNoteStore : NoteStore {
    private val notes = ConcurrentSkipListMap<Long, Note>()
    private val nextId = AtomicLong(1)

    override suspend fun all(): List<Note> = notes.values.toList()

    override suspend fun add(text: String): Note =
        Note(nextId.getAndIncrement(), text).also { notes[it.id] = it }

    override suspend fun remove(id: Long) {
        notes.remove(id)
    }
}
