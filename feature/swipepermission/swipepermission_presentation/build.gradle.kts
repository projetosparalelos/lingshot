plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.swipepermission_presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":domain"))

    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.permissions)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.lottie.compose)
    implementation(libs.toasty)
}
