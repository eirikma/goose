package goose

import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.UL
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.ul

fun HTML.indexPage(notes: List<Note>) {
    attributes["lang"] = "no"
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        meta(name = "theme-color", content = "#2f6f4f")
        title("Goose")
        link(rel = "manifest", href = "/manifest.webmanifest")
        link(rel = "icon", href = "/static/icons/icon.svg", type = "image/svg+xml")
        link(rel = "apple-touch-icon", href = "/static/icons/icon-192.png")
        link(rel = "stylesheet", href = "/static/css/app.css")
        script(src = "/static/js/htmx.min.js") { defer = true }
        script(src = "/static/js/app.js") { defer = true }
    }
    body {
        main {
            h1 { +"Goose" }
            p("lead") { +"Ktor + htmx + PWA – startmal." }
            form {
                attributes["hx-post"] = "/notes"
                attributes["hx-target"] = "#notes"
                attributes["hx-swap"] = "outerHTML"
                attributes["hx-on::after-request"] = "if (event.detail.successful) this.reset()"
                input(type = InputType.text, name = "text") {
                    placeholder = "Nytt notat"
                    required = true
                    attributes["maxlength"] = "500"
                    attributes["autocomplete"] = "off"
                }
                button(type = ButtonType.submit) { +"Legg til" }
            }
            ul { noteList(notes) }
        }
    }
}

/** Brukes både i hele siden og som htmx-fragment, så markupen er lik begge steder. */
fun UL.noteList(notes: List<Note>) {
    id = "notes"
    if (notes.isEmpty()) {
        li("empty") { +"Ingen notater ennå." }
    }
    notes.forEach { note ->
        li {
            span { +note.text }
            button(type = ButtonType.button) {
                attributes["hx-delete"] = "/notes/${note.id}"
                attributes["hx-target"] = "#notes"
                attributes["hx-swap"] = "outerHTML"
                attributes["aria-label"] = "Slett"
                +"✕"
            }
        }
    }
}
