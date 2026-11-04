# --- Bygg ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src

# Last ned Gradle og avhengigheter i et eget lag, så de caches mellom bygg
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle gradle
RUN ./gradlew --no-daemon dependencies > /dev/null

COPY src src
RUN ./gradlew --no-daemon installDist -x test

# --- Kjøring ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app \
    && mkdir /data && chown app:app /data
WORKDIR /app
COPY --from=build /src/build/install/goose /app
USER app

# Små containere: la JVM-en bruke 75 % av containerens minne, og SerialGC
# har minst overhead når det bare er én CPU.
ENV PORT=8080 \
    DB_PATH=/data/goose.db \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k"
EXPOSE 8080
VOLUME ["/data"]
CMD ["/app/bin/goose"]
