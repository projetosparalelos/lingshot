plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.designsystem"
}

dependencies {
    implementation(libs.accompanist.webview)
}
