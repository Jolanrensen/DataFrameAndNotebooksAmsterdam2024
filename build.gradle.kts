plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlinx.dataframe") version "0.13.1"
}

group = "nl.jolanrensen.dataFrameAndNotebooks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:dataframe:0.13.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}