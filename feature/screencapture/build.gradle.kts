plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.screencapture"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.activity.compose)
}