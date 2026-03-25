plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.libraryconsole.MainKt")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnit()
}

dependencies {
    testImplementation(kotlin("test"))
}

