@file:Suppress("UNUSED")

import extension.getLibrary
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply {
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                add("implementation", libs.getLibrary("dagger-hilt-android"))
                add("ksp", libs.getLibrary("dagger-hilt-compiler"))
            }
        }
    }
}
