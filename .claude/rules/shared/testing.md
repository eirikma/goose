---
paths:
  - "src/test/**"
---

# Testing (Kotlin/JVM)

- TDD: red → green → refactor. Write the failing test first and watch it fail for the right
  reason.
- Use `kotlin.test` on the JUnit 5 platform. Test names are backtick sentences that describe
  behaviour.
- Prefer hand-written fakes (an in-memory implementation of the interface) over mocking
  libraries.
- Test through public APIs: HTTP via `testApplication`, stores via their interface.
- Each test builds its own fixture. Use temp directories for files. Never share mutable state
  between tests.
- One behaviour per test. Assertions should make a failure self-explanatory (`assertEquals`
  on whole values rather than on a boolean).
- When several tests differ only in input and expected output, propose a table-driven test
  (TableTest) instead of copy-pasting. Invoke the `tabletest` skill before writing one.
