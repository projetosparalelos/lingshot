plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.screencapture"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.activity.compose)
    implementation(libs.toasty)
}
