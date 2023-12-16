plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
    id("lingshot.android.network.plugin")
}

android {
    namespace = "com.lingshot.remote"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)

    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)
}
