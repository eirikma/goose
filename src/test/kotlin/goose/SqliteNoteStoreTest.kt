package goose

import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class SqliteNoteStoreTest {
    @Test
    fun `notater overlever at databasen åpnes på nytt`() = runBlocking {
        val file = Files.createTempDirectory("goose").resolve("sub/goose.db")

        SqliteNoteStore(file).use { store ->
            val a = store.add("første")
            store.add("andre")
            store.remove(a.id)
        }

        SqliteNoteStore(file).use { store ->
            assertEquals(listOf("andre"), store.all().map { it.text })
            // Migreringen skal tåle å kjøre igjen, og id-er skal ikke gjenbrukes.
            val c = store.add("tredje")
            assertEquals(3, c.id)
        }
    }
}
