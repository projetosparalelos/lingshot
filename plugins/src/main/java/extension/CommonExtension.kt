@file:Suppress("UnstableApiUsage")

package extension

import AppVersionPlugin.Companion.javaCompileVersion
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.getVersion("composeCompiler")
        }

        packagingOptions {
            resources.excludes.apply {
                add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }
    }
}

internal fun Project.configureKotlinJvm(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        compileOptions {
            sourceCompatibility = javaCompileVersion
            targetCompatibility = javaCompileVersion
        }

        project.tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = javaCompileVersion.toString()
            }
        }
    }
}