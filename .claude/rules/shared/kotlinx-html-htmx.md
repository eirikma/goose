---
paths:
  - "**/*.kt"
  - "**/static/**"
---

# Server-rendered HTML with kotlinx.html and htmx

## Views
- Views are pure extension functions on kotlinx.html tags (`fun HTML.indexPage(...)`,
  `fun UL.noteList(...)`). They take data and render it. They never fetch data or touch stores.
- A fragment that htmx swaps in is rendered by the **same** function that renders it inside
  the full page, so the two can never drift apart.
- Text goes in with `+text`, which kotlinx.html escapes. Never put user data inside `unsafe {}`.

## htmx endpoints
- Mutating endpoints (`POST`, `PUT`, `DELETE`) return only the HTML fragment to swap, built with
  `createHTML()` and sent as `ContentType.Text.Html`.
- Target by element id. Prefer `hx-swap="outerHTML"` with the fragment's root element
  carrying that same id, so repeated swaps keep working.
- Write `hx-*` attributes through `attributes["hx-..."]`. Extract a small helper once the same
  combination appears three times.
- Do not add client-side JavaScript for things htmx attributes can do.

## Assets and accessibility
- htmx is vendored as a static file. Upgrade by replacing the file. There is no npm or JS build.
- Set `lang` on `<html>`. Icon-only buttons need an `aria-label`. Inputs need a `name` and
  sensible `maxlength` matching server-side limits.
