package goose

import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `forsiden svarer og lenker til manifestet`() = testApplication {
        application { module(InMemoryNoteStore()) }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "/manifest.webmanifest")
    }

    @Test
    fun `nytt notat returnerer oppdatert liste`() = testApplication {
        application { module(InMemoryNoteStore()) }
        val response = client.submitForm("/notes", parameters { append("text", "Hei fra test") })
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "Hei fra test")
    }

    @Test
    fun `pwa-filer og health er tilgjengelige`() = testApplication {
        application { module(InMemoryNoteStore()) }
        assertEquals(HttpStatusCode.OK, client.get("/sw.js").status)
        assertEquals(HttpStatusCode.OK, client.get("/manifest.webmanifest").status)
        assertEquals(HttpStatusCode.OK, client.get("/static/js/htmx.min.js").status)
        assertEquals("ok", client.get("/health").bodyAsText())
    }
}
