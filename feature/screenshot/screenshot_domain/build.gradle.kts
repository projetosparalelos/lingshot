plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.screenshot_domain"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
}