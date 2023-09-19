@file:Suppress("UNUSED")

import extension.getBundle
import extension.getLibrary
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply {
                apply("app.cash.paparazzi")
            }
            dependencies {
                add("implementation", libs.getLibrary("core-ktx"))
                add("implementation", libs.getLibrary("appcompat"))
                add("implementation", libs.getLibrary("material"))
                add("implementation", libs.getLibrary("kotlinx-collections-immutable"))
                add("implementation", libs.getBundle("lifecyle"))
                add("implementation", libs.getBundle("coroutines"))
                add("implementation", libs.getLibrary("timber"))

                add("testImplementation", libs.getBundle("testing-unit"))
            }
        }
    }
}
