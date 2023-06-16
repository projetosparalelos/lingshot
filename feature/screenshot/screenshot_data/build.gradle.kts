@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

apply {
    from("$rootDir/plugins/app-versions.gradle")
    from("$rootDir/plugins/android-library.gradle")
}

android {
    namespace = "com.teachmeprint.screenshot_data"
}

dependencies {
    implementation(project(":feature:screenshot:screenshot_domain"))

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}
