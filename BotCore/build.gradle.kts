plugins {
    kotlin("jvm") version "1.6.0"
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.arsoban"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":KordexBot"))
    implementation(project(":JavacordBot"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}