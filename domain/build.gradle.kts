plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.domain"
}

dependencies {
    implementation(project(":feature:languagechoice:languagechoice_domain"))

    implementation(libs.timber)
}
