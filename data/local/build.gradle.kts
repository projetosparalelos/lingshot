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
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(libs.text.recognition)
    implementation(libs.text.recognition.chinese)
    implementation(libs.text.recognition.japanese)
    implementation(libs.text.recognition.korean)
    implementation(libs.language.id)

    implementation(libs.datastore.preferences)

    implementation(libs.bundles.coroutines)
    implementation(libs.timber)
}
