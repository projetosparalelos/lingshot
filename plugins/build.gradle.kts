plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    implementation(libs.detekt.plugin)
    implementation(libs.spotless.plugin)
}

gradlePlugin {
    plugins {
        register("appVersionPlugin") {
            id = "lingshot.app.version.plugin"
            implementationClass = "AppVersionPlugin"
        }
        register("androidApplicationPlugin") {
            id = "lingshot.android.application.plugin"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidLibraryPlugin") {
            id = "lingshot.android.library.plugin"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidLibraryComposePlugin") {
            id = "lingshot.android.library.compose.plugin"
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("androidHiltPlugin") {
            id = "lingshot.android.hilt.plugin"
            implementationClass = "AndroidHiltPlugin"
        }
        register("androidQualityPlugin") {
            id = "lingshot.android.quality.plugin"
            implementationClass = "AndroidQualityPlugin"
        }
        register("androidFirebase") {
            id = "lingshot.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidAdmobPlugin") {
            id = "lingshot.android.admob.plugin"
            implementationClass = "AndroidAdmobPlugin"
        }
        register("androidNetworkPlugin") {
            id = "lingshot.android.network.plugin"
            implementationClass = "AndroidNetworkPlugin"
        }
    }
}
