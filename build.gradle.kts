plugins {
    kotlin("jvm") version "2.4.21"
    application
}

group = "goose"
version = "0.1.0"

repositories {
    mavenCentral()
}

val ktorVersion = "3.6.1"

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    // CIO er ren Kotlin og bruker mindre minne enn Netty – fint for små containere.
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-compression:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.6.6")
    // Inneholder native biblioteker for macOS, Linux (glibc og musl/Alpine) og Windows.
    implementation("org.xerial:sqlite-jdbc:3.54.1.0")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("goose.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}
