plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.quality.plugin")
}

android {
    namespace = "com.teachmeprint.languagechoice_domain"
}

dependencies {
    implementation(libs.bundles.coroutines)
}
