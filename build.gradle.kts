import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.mirbor"
version = "1.0"

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)

    //load svg
    implementation("org.apache.xmlgraphics:batik-all:1.13")
    implementation("br.com.devsrsouza.compose.icons.jetbrains:font-awesome:1.0.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.github.pengrad:java-telegram-bot-api:5.2.0")

    //networking
    implementation ("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation ("com.github.kittinunf.fuel:fuel-gson:2.3.1")

    //html parsing
    implementation ("org.jsoup:jsoup:1.14.3")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "QueryDesktopAppAndBot"
            packageVersion = "1.0.0"
        }
    }
}