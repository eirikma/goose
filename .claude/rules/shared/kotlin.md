---
paths:
  - "**/*.kt"
  - "**/*.kts"
---

# Kotlin

Idiomatic Kotlin, applying the general principles (single responsibility, immutability, pure
functions) in Kotlin's own terms.

## Style
- Official Kotlin code style (`kotlin.code.style=official`), formatted with the IntelliJ formatter.
- Explicit imports; no wildcard imports.
- Type inference for local values. Explicit types on public functions and public properties,
  except trivially obvious single-expression bodies.
- KDoc on classes and interfaces stating their single responsibility. Avoid other comments
  unless they explain *why*.

## Immutability and data
- `val` over `var`. A `var` needs a reason.
- Read-only collection types (`List`, `Map`) in APIs; keep mutable ones local and private.
- `data class` for values, `sealed interface` for closed sets of alternatives, exhaustive `when`
  without `else` so new cases fail compilation.
- Prefer top-level pure functions and extension functions over utility classes.

## Null safety and errors
- Never use `!!`. Use `?:`, `?.let`, `requireNotNull(x) { "message" }` or `checkNotNull`.
- `require` for argument preconditions, `check` for state. Messages must say what was wrong
  and with which value.
- Do not swallow exceptions. Catch only what you can handle, at a boundary.

## Functions
- Small and focused. Prefer expression bodies when the function fits on one or two lines.
- Scope functions sparingly: `use` for resources, `also`/`let` when it reads better. Never nest
  them.
- Prefer `buildList`/`map`/`filter` and other collection functions over manual loops, unless
  the loop is clearly simpler (e.g. iterating a JDBC `ResultSet`).

## Coroutines
- Blocking I/O (JDBC, files) runs inside `withContext(Dispatchers.IO)`.
- No `runBlocking` in production code and no `GlobalScope`. `runBlocking` is fine in tests.
- Interfaces for I/O-bound collaborators expose `suspend` functions.
