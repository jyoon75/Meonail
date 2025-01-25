plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Safe Args
buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.4"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
