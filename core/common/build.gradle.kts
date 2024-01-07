
plugins {
    id("lingshot.app.version.plugin")
    id("lingshot.android.library.plugin")
    id("lingshot.android.quality.plugin")
}

android {
    namespace = "com.lingshot.common"

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
    implementation(project(":domain"))

    implementation(libs.retrofit)
}
