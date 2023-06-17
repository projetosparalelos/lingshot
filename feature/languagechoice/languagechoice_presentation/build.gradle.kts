plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.library.compose.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.languagechoice_presentation"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
}