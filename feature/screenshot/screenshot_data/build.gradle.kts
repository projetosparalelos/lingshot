plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.screenshot_data"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":feature:screenshot:screenshot_domain"))

    implementation(libs.text.recognition)
    implementation(libs.language.id)
}
