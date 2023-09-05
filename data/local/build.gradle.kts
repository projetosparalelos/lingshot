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

    implementation(libs.datastore.preferences)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
}
