plugins {
    kotlin("jvm") version "1.4.30"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/kscience")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("kscience.plotlykt:plotlykt-server:0.3.0")
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}
