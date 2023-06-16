@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.android)
}

apply {
    from("$rootDir/plugins/app-versions.gradle")
}

android {
    namespace = "com.teachmeprint.screenshot_domain"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.kotlinx.serialization.json)

    api(libs.text.recognition)
    api(libs.language.id)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}