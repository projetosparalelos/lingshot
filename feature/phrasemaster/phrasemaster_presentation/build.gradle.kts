plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.phrasemaster_presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":domain"))
    implementation(project(":feature:phrasemaster:phrasemaster_domain"))

    implementation(libs.hilt.navigation.compose)

    implementation(libs.compose.markdown)
}
