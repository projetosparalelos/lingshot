plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.phrasemaster_domain"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
}