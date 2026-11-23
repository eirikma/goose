---
paths:
  - "**/*.kt"
---

# Ktor (server)

## Wiring
- `main()` reads configuration from environment variables with sensible local defaults and
  starts `embeddedServer(...)`. Nothing else lives in `main()`.
- `fun Application.module(dep: Dep = defaultDep())` installs plugins and routing.
  Collaborators are parameters with production defaults, so tests can pass fakes. No DI
  framework.
- Close resources on `monitor.subscribe(ApplicationStopped) { ... }`.
- Prefer the CIO engine for small containers unless a feature requires Netty.

## Routes
- Handlers are thin: parse and validate input → call domain/store → respond. Move any logic
  that is more than a line or two into named functions.
- Validate at the boundary: `toLongOrNull()`, `trim()`, length limits. Invalid input gets
  `400 Bad Request` with a short message, never a 500.
- Shared response helpers are private extension functions on `RoutingCall`.
- Load classpath resources once at module start with a clear failure message when one is
  missing, not on every request.

## Testing
- Test through HTTP with `testApplication { application { module(fake) } }`.
- Assert both status and the relevant part of the body.
- Use real implementations behind the interface only in their own focused tests.
