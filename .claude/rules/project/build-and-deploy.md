# Goose – bygg og drift

## Kommandoer
```sh
./gradlew build        # porten: kompilering + tester, må være grønn uten advarsler
./gradlew test         # bare tester
./gradlew run          # http://localhost:8080
docker build -t goose . && docker run --rm -p 8080:8080 goose
```
Java 21 (Temurin) overalt: Gradle-toolchain, CI og Docker-images.

## CI og deploy (`.github/workflows/ci.yml`)
- Pull request: `test`.
- Push til `main`: `test` → `image` (pusher `ghcr.io/eirikma/goose:latest` og `:<sha>`) og
  `deploy-fly` (`flyctl deploy --remote-only`).
- **Merge til `main` er det samme som produksjonsdeploy.**
- Hemmeligheter: `FLY_API_TOKEN` (GitHub Actions secret). `deploy-vps` er kommentert ut.

## Fly.io (primær drift)
- App `goose-eirikma`, region `arn`, `shared-cpu-1x` / 512 MB, auto-stopp (kald start tar noen
  sekunder).
- Volum `goose_data` montert på `/data`. Helsesjekk: `GET /health`.

## Alternativ: VPS med Docker Compose + Caddy
- `docker-compose.yml` + `Caddyfile`, `DOMAIN` i `.env` (se `.env.example`).

## Regler for agenter
- Kjør aldri `fly deploy`, `fly volumes …`, `docker compose` mot servere eller noe annet som
  endrer produksjon, uten at mennesket eksplisitt ber om det.
- Endringer i `ci.yml`, `Dockerfile` og `fly.toml` påvirker produksjon. Beskriv konsekvensen i
  planen.
