plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.phrasemaster_data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":domain"))
    implementation(project(":feature:phrasemaster:phrasemaster_domain"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
}
