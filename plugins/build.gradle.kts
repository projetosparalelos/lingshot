plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.ktlint.plugin)
}

gradlePlugin {
    plugins {
        register("appVersionPlugin") {
            id = "teachmeprint.app.version.plugin"
            implementationClass = "AppVersionPlugin"
        }
        register("androidApplicationPlugin") {
            id = "teachmeprint.android.application.plugin"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidLibraryPlugin") {
            id = "teachmeprint.android.library.plugin"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidLibraryComposePlugin") {
            id = "teachmeprint.android.library.compose.plugin"
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("androidHiltPlugin") {
            id = "teachmeprint.android.hilt.plugin"
            implementationClass = "AndroidHiltPlugin"
        }
        register("androidQualityPlugin") {
            id = "teachmeprint.android.quality.plugin"
            implementationClass = "AndroidQualityPlugin"
        }
        register("androidAdmobPlugin") {
            id = "teachmeprint.android.admob.plugin"
            implementationClass = "AndroidAdmobPlugin"
        }
        register("androidNetworkPlugin") {
            id = "teachmeprint.android.network.plugin"
            implementationClass = "AndroidNetworkPlugin"
        }
    }
}