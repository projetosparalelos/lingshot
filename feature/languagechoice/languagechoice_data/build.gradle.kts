plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.languagechoice_data"
}

dependencies {
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(libs.datastore.preferences)
}
