plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.reviewlevel_domain"
}

dependencies {
    implementation(project(":domain"))
}
