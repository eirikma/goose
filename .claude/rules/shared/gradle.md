---
paths:
  - "**/*.gradle.kts"
  - "gradle.properties"
  - "gradle/**"
---

# Gradle (Kotlin DSL)

- Always use the wrapper (`./gradlew`), never a system-installed Gradle.
- `./gradlew build` is the gate: it compiles, runs tests and checks. It must be green and
  free of warnings.
- Pin the JVM with `kotlin { jvmToolchain(N) }`. It must match the CI and Docker images.
- Keep versions in one place: one `val` per library family (e.g. `ktorVersion`) in a
  single-module build. Move to a version catalogue (`gradle/libs.versions.toml`) when a second
  module appears.
- New dependencies are a design decision: discuss before adding. Add a short comment saying
  *why* when the choice is not obvious.
- Prefer the Kotlin/Ktor ecosystem (`kotlin("test")`, `ktor-server-test-host`) over extra
  libraries.
