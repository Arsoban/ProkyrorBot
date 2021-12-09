plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10-RC"
}

group = "com.com.arsoban"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.5.1-RC1")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation("io.ktor:ktor-client-serialization:1.6.7")
    implementation("ch.qos.logback:logback-classic:1.2.7")
    implementation("io.ktor:ktor-client-logging:1.6.7")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}