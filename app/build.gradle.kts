plugins {
    id("lingshot.android.application.plugin")
    id("lingshot.android.hilt.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")

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

        setProperty("archivesBaseName", "${parent?.name}-$versionName")
    }

    buildFeatures {
        buildConfig = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../config/signing/lingshot-keystore")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
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
    implementation(project(":feature:screenshot:screenshot_domain"))
    implementation(project(":feature:screenshot:screenshot_presentation"))
    implementation(project(":feature:swipepermission:swipepermission_presentation"))

    implementation(libs.compose.material3)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.navigation.animation)

    implementation(libs.core.splash.screen)
    implementation(libs.toasty)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.play.services.ads)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
