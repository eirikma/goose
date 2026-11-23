# Goose – prosjektoversikt

Goose er en liten PWA i Kotlin: **Ktor (CIO) + kotlinx.html + htmx**, med SQLite som lagring,
pakket som container. Foreløpig inneholder den et notat-eksempel som viser htmx-mønsteret.

## Kodekart
```
src/main/kotlin/goose/
  Application.kt   main(), Application.module(store), ruter, PWA-endepunkter
  Views.kt         kotlinx.html-visninger: hele siden + htmx-fragmenter
  NoteStore.kt     Note, NoteStore-grensesnitt, SqliteNoteStore, InMemoryNoteStore
src/main/resources/
  pwa/             sw.js og manifest.webmanifest (serveres fra roten)
  static/          css, js (htmx er vendret inn), ikoner
src/test/kotlin/goose/
  ApplicationTest.kt      HTTP-tester mot module(InMemoryNoteStore())
  SqliteNoteStoreTest.kt  tester mot ekte SQLite-fil
```

## Domenespråk
- **Notat** (`Note`): en kort tekst (maks 500 tegn) med id.

## Språkkonvensjoner
- Identifikatorer (klasser, funksjoner, variabler) skrives på **engelsk**.
- Kommentarer, KDoc, testnavn, feilmeldinger til brukeren og all tekst i UI skrives på
  **norsk** (bokmål).
- README og prosjektregler (`.claude/rules/project/`) skrives på norsk. Gjenbrukbare regler
  (`.claude/rules/shared/`) skrives på engelsk.
- `<html lang="no">`.

## Prosjektspesifikke valg
- Databasefil: `DB_PATH`, lokalt `./data/goose.db` (ignorert av git), i container
  `/data/goose.db` på et volum.
- Nye migreringssteg legges til i `SqliteNoteStore.migrate()`.
- Endres noe under `static/`, skal `CACHE` i `src/main/resources/pwa/sw.js` økes (`goose-vN`).
