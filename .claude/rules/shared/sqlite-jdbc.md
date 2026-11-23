---
paths:
  - "**/*.kt"
  - "**/*.sql"
---

# SQLite with plain JDBC

No ORM and no connection pool. SQLite has a single writer anyway.

## Access
- Storage sits behind an interface with `suspend` functions. The SQLite implementation is one
  class. An in-memory implementation of the same interface is the fake for route tests.
- One open connection, serialised with `synchronized`, executed on `Dispatchers.IO`.
- Set `PRAGMA journal_mode=WAL` and `PRAGMA busy_timeout` when opening.
- Always `prepareStatement` with `?` parameters. Never build SQL from input strings.
- Wrap every `Statement`, `PreparedStatement` and `ResultSet` in `use { }`.
- Use `INSERT ... RETURNING id` to get generated keys.
- The database file path comes from an environment variable with a local default. Create
  parent directories on open.

## Migrations
- Schema version lives in `PRAGMA user_version`.
- Migrations are numbered steps guarded by `if (version < n)`, applied in order, each ending
  with `PRAGMA user_version = n`.
- Append new steps. **Never edit a step that has been deployed.**
- Every step must be safe to run on startup against both an empty and an up-to-date database.

## Testing
- Test the SQLite implementation against a real file in a temp directory.
- Include a reopen test (close, open the same file again) to prove that migrations are
  idempotent and data survives.

## Operations
- Back up a live database with `sqlite3 <db> ".backup <file>"`, not with a plain file copy.
