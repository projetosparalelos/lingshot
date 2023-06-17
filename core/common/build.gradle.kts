@file:Suppress("UnstableApiUsage")

plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")
    id("teachmeprint.android.admob.plugin")
}

android {
    namespace = "com.teachmeprint.common"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.play.services.ads)
}