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

    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:text-recognition-chinese:16.0.0")
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.0")
    implementation("com.google.mlkit:text-recognition-japanese:16.0.0")
    implementation("com.google.mlkit:text-recognition-korean:16.0.0")

    implementation("com.google.mlkit:language-id:17.0.4")

    implementation(libs.datastore.preferences)

    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
}
