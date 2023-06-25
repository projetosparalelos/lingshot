@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("teachmeprint.android.application.plugin")
    id("teachmeprint.android.hilt.plugin")
    id("teachmeprint.android.library.plugin")
    id("teachmeprint.android.quality.plugin")

    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = AppVersionPlugin.APPLICATION_NAME_ID
    compileSdk = AppVersionPlugin.COMPILE_SDK

    defaultConfig {
        applicationId = AppVersionPlugin.APPLICATION_NAME_ID

        versionCode = AppVersionPlugin.VERSION_CODE
        versionName = AppVersionPlugin.VERSION_NAME

        minSdk = AppVersionPlugin.MIN_SDK
        targetSdk = AppVersionPlugin.TARGET_SDK
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":domain"))
    implementation(project(":feature:home:home_presentation"))
    implementation(project(":feature:languagechoice:languagechoice_data"))
    implementation(project(":feature:languagechoice:languagechoice_domain"))
    implementation(project(":feature:languagechoice:languagechoice_presentation"))
    implementation(project(":feature:screencapture"))
    implementation(project(":feature:screenshot:screenshot_data"))
    implementation(project(":feature:screenshot:screenshot_domain"))
    implementation(project(":feature:screenshot:screenshot_presentation"))
    implementation(project(":feature:swipepermission:swipepermission_presentation"))

    implementation(libs.compose.material3)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.navigation.compose)
    implementation(libs.core.splash.screen)
    implementation(libs.hawk)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)

    implementation(libs.play.services.ads)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}