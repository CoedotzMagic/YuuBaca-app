buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.12.1") // Pastikan versinya sesuai (kamu tulis 8.12.1, mungkin maksudnya 8.1.2?)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.google.gms:google-services:4.4.3")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.6")
        classpath("com.google.firebase:perf-plugin:2.0.1")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}