plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.languagechoice_presentation"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
}
