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
                apply("org.jetbrains.kotlin.kapt")
            }

            val extension = extensions.getByType<LibraryExtension>()

            extension.apply {
                compileSdk = COMPILE_SDK

                defaultConfig {
                    minSdk = MIN_SDK
                }
                configureKotlinJvm(this)
            }
        }
    }

  companion object {
    const val APPLICATION_NAME_ID = "com.lingshot.language"
    const val VERSION_CODE = 5
    const val VERSION_NAME = "1.5"

    const val COMPILE_SDK = 33
    const val TARGET_SDK = 33
    const val MIN_SDK = 24
    val javaCompileVersion = JavaVersion.VERSION_1_8
  }
}
