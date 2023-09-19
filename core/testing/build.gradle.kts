plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.testing"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        )
    }
}

dependencies {
    implementation(project(":core:designsystem"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.test)

    implementation(libs.paparazzi)
}
