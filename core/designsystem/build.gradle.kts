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
    implementation(project(":core:common"))

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.webview)
    implementation(libs.image.cropper)
    implementation(libs.lottie.compose)
}
