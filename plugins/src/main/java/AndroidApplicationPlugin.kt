@file:Suppress("UNUSED")

import com.android.build.api.dsl.ApplicationExtension
import extension.configureAndroidCompose
import extension.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
            }
            val extension = extensions.getByType<ApplicationExtension>()

            extension.apply {
                configureKotlinJvm(this)
                configureAndroidCompose(this)
            }
        }
    }

}