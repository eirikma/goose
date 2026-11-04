package goose

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.header
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.routing.RoutingCall
import kotlinx.html.stream.createHTML
import kotlinx.html.ul

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(CIO, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(store: NoteStore = defaultNoteStore()) {
    install(CallLogging)
    monitor.subscribe(ApplicationStopped) { (store as? AutoCloseable)?.close() }
    install(Compression)

    // Service worker og manifest må ligge på roten for å kontrollere hele appen.
    val serviceWorker = resourceText("pwa/sw.js")
    val manifest = resourceText("pwa/manifest.webmanifest")

    routing {
        get("/health") { call.respondText("ok") }

        get("/sw.js") {
            call.response.header(HttpHeaders.CacheControl, "no-cache")
            call.respondText(serviceWorker, ContentType.Text.JavaScript)
        }
        get("/manifest.webmanifest") {
            call.respondText(manifest, ContentType.parse("application/manifest+json"))
        }
        staticResources("/static", "static")

        get("/") {
            val notes = store.all()
            call.respondHtml { indexPage(notes) }
        }

        // htmx-endepunkter: returnerer bare HTML-fragmentet som skal byttes ut.
        post("/notes") {
            val text = call.receiveParameters()["text"]?.trim().orEmpty()
            if (text.isNotEmpty()) store.add(text.take(500))
            call.respondNoteList(store)
        }
        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Ugyldig id", status = HttpStatusCode.BadRequest)
                return@delete
            }
            store.remove(id)
            call.respondNoteList(store)
        }
    }
}

private suspend fun RoutingCall.respondNoteList(store: NoteStore) {
    val notes = store.all()
    val fragment = createHTML().ul { noteList(notes) }
    respondText(fragment, ContentType.Text.Html)
}

private fun resourceText(path: String): String =
    requireNotNull(Application::class.java.classLoader.getResource(path)) { "Mangler ressurs: $path" }
        .readText()
