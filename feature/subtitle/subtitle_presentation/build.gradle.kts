plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.subtitle_presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))
    implementation(project(":domain"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
    implementation(project(":feature:languagechoice:languagechoice_presentation"))
    implementation(project(":feature:screencapture"))

    implementation(libs.activity.compose)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.toasty)

    implementation(libs.image.cropper)
    implementation(libs.lottie.compose)
    implementation(libs.telephoto.zoomable)
}
