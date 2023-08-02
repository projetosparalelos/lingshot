plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.local"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.text.recognition)
    implementation(libs.language.id)

    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
}
