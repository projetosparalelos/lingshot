plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.library.compose.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.home_presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:home:home_domain"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
    implementation(project(":feature:phrasemaster:phrasemaster_domain"))

    testImplementation(project(":core:testing"))

    implementation(libs.accompanist.navigation.animation)
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.hilt.navigation.compose)
}
