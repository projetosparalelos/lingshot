plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.navigation"
}

dependencies {
    implementation(project(":core:common"))
}