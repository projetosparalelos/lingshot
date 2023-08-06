plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.completephrase_presentation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))

    implementation(libs.accompanist.navigation.animation)

    implementation(libs.hilt.navigation.compose)
}
