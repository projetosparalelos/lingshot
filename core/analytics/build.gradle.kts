plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.analytics"
}

dependencies {
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
