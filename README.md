# Goose

Startmal for en liten PWA i Kotlin: **Ktor (CIO) + htmx + kotlinx.html**, pakket som container.
Inneholder et enkelt notat-eksempel som viser htmx-mønsteret (POST/DELETE som returnerer HTML-fragmenter).

## Kjør lokalt

```sh
./gradlew run          # http://localhost:8080
./gradlew test
```

Med Docker:

```sh
docker build -t goose .
docker run --rm -p 8080:8080 goose
```

## Struktur

```
src/main/kotlin/goose/
  Application.kt   ruter, oppstart, PWA-endepunkter
  Views.kt         HTML med kotlinx.html (hele siden + htmx-fragmenter)
  NoteStore.kt     lagring: SQLite via JDBC (+ in-memory for tester)
src/main/resources/
  pwa/             manifest.webmanifest og sw.js (serveres fra roten)
  static/          css, js (htmx er vendret inn), ikoner
```

## Database

SQLite via ren JDBC (`org.xerial:sqlite-jdbc`), i WAL-modus. Filen styres av `DB_PATH`:
lokalt blir den `./data/goose.db` (ignorert av git), i containeren `/data/goose.db` på et volum.
Skjemaet migreres ved oppstart med `PRAGMA user_version` – legg nye steg til i
`SqliteNoteStore.migrate()`.

Backup er bare å kopiere filen, helst med `sqlite3 goose.db ".backup backup.db"` mens appen kjører.

PWA-krav: appen må serveres over HTTPS (localhost er unntatt) for at service workeren
skal registreres og appen kan installeres. Både Fly.io og Caddy gir deg det automatisk.

## Deploy alternativ 1: Fly.io

```sh
fly launch --no-deploy   # bruk eksisterende fly.toml, velg unikt appnavn
fly volumes create goose_data --size 1 --region arn
fly deploy
```

`fly.toml` er satt opp med 512 MB og auto-stopp, så maskinen sover når ingen bruker appen.
Første forespørsel etter dvale tar noen sekunder (JVM-oppstart).

## Deploy alternativ 2: Egen VPS med Docker Compose + Caddy

På serveren (med Docker installert og DNS pekt mot den):

```sh
git clone git@github.com:eirikma/goose.git && cd goose
cp .env.example .env     # sett DOMAIN
docker compose up -d
```

CI (`.github/workflows/ci.yml`) bygger og pusher `ghcr.io/eirikma/goose:latest` ved push til `main`.
Deretter er oppdatering `docker compose pull && docker compose up -d`, eventuelt automatisk
via deploy-jobben som ligger kommentert ut i workflowen.

## Neste steg

- Automatisk backup av SQLite-filen, f.eks. med Litestream til S3-kompatibel lagring.
- Husk å øke `CACHE`-navnet i `sw.js` når statiske filer endres.
