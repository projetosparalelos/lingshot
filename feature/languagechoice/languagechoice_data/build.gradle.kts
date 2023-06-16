@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

apply {
    from("$rootDir/plugins/app-versions.gradle")
}

android {
    namespace = "com.teachmeprint.languagechoice_data"
}

dependencies {
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(libs.hawk)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}