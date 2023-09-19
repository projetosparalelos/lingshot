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

    implementation(libs.paparazzi) {
        exclude(group = "androidx.annotation", module = "annotation-jvm")
        exclude(group = "app.cash.paparazzi", module = "layoutlib-native-jdk11")
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.sun.activation", module = "javax.activation")
        exclude(group = "org.hamcrest", module = "hamcrest-core")

        exclude(group = "com.android.tools", module = "sdklib")
        exclude(group = "com.android.tools", module = "repository")

        exclude(group = "org.glassfish.jaxb", module = "jaxb-runtime")
        exclude(group = "org.jvnet.staxex", module = "stax-ex")
        exclude(group = "jakarta.xml.bind", module = "jakarta.xml.bind-api")
        exclude(group = "org.glassfish.jaxb", module = "txw2")
        exclude(group = "com.sun.istack", module = "istack-commons-runtime")
        exclude(group = "jakarta.activation", module = "jakarta.activation-api")
    }
}
