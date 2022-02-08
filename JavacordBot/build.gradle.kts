plugins {
    java
}

group = "me.desinger"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("org.javacord:javacord:3.4.0")
    implementation("com.sedmelluq:lavaplayer:1.3.78")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}