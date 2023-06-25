plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.library.compose.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.swipepermission_presentation"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":domain"))

    implementation(libs.accompanist.permissions)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.lottie.compose)
}