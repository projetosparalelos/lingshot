plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.screenshot_data"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":feature:screenshot:screenshot_domain"))

    implementation(libs.text.recognition)
    implementation(libs.language.id)
}
