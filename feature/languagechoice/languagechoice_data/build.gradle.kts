plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.languagechoice_data"
}

dependencies {
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(libs.hawk)
}