plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.screenshot_data"
}

dependencies {
    implementation(project(":feature:screenshot:screenshot_domain"))

    implementation(libs.datastore.preferences)
}
