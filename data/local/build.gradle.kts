plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.local"
}

dependencies {
    implementation(libs.hawk)
}