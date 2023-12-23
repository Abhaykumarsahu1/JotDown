// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories{
        google()
        mavenCentral()
    }

    dependencies{
        classpath ("com.android.tools.build:gradle:7.0.4")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath ("com.android.tools.build:gradle:4.1.3") // Use a compatible AGP version

    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}