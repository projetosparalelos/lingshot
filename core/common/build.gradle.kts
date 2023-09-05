plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
    id("lingshot.android.admob.plugin")
}

android {
    namespace = "com.lingshot.common"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.retrofit)
    implementation(libs.play.services.ads)
}
