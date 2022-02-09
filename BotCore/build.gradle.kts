plugins {
    kotlin("jvm") version "1.6.10"
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
    implementation(project(":RestApi"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.create<DefaultTask>("stage") {
    dependsOn("build", "shadowJar")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        manifest {
            attributes("Main-Class" to "com.arsoban.BotsMainKt")
        }
    }
}