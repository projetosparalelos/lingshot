@file:Suppress("UNUSED")

import com.android.build.gradle.LibraryExtension
import extension.configureKotlinJvm
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AppVersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }
            val extension = extensions.getByType<LibraryExtension>()

            extension.apply {
                compileSdk = COMPILE_SDK

                defaultConfig {
                    minSdk = MIN_SDK
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles("proguard-android.txt", "proguard-rules.pro")
                        consumerProguardFiles("proguard-rules.pro")
                    }
                }

                buildFeatures {
                    buildConfig = true
                }
                configureKotlinJvm(this)
            }
        }
    }

  companion object {
    const val APPLICATION_NAME_ID = "com.lingshot.languagelearn"
    const val VERSION_CODE = 20
    const val VERSION_NAME = "1.2.0"

    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 26
    val javaCompileVersion = JavaVersion.VERSION_17
  }
}
