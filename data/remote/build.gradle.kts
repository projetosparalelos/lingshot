plugins {
    id("teachmeprint.app.version.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")
    id("teachmeprint.android.network.plugin")
}

android {
    namespace = "com.teachmeprint.remote"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)

    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)
}