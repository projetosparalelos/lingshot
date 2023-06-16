@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

apply {
    from("$rootDir/plugins/app-versions.gradle")
}

android {
    namespace = "com.teachmeprint.domain"
}