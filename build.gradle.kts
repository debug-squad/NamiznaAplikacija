import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.6.10"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.github.kittinunf.fuel:fuel:2.3.1")
                implementation("com.github.kittinunf.fuel:fuel-json:2.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("com.soywiz.korlibs.klock:klock-jvm:2.7.0")
                implementation("com.google.code.gson:gson:2.8.5")
                implementation("org.litote.kmongo:kmongo:4.6.0")
                implementation("org.litote.kmongo:kmongo-async:4.6.0")
                implementation("org.mongodb:mongodb-driver-async:3.12.2")
                implementation("org.mongodb:mongodb-driver-reactivestreams:1.6.0")
            }
        }
        val jvmTest by getting
    }
}



compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Namizna_Aplikacija"
            packageVersion = "1.0.0"
        }
    }
}
