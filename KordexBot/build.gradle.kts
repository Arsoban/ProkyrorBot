plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "com.com.arsoban"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.5.2-RC1")
    implementation("org.slf4j:slf4j-simple:1.7.35")
    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation("io.ktor:ktor-client-serialization:1.6.7")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("io.ktor:ktor-client-logging:1.6.7")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}