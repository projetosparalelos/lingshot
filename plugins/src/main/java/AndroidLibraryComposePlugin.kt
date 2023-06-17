@file:Suppress("UNUSED")

import com.android.build.gradle.LibraryExtension
import extension.configureAndroidCompose
import extension.getLibrary
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)

            dependencies {
                add("implementation", platform(libs.getLibrary("compose-bom")))
                add("implementation", libs.getLibrary("compose-foundation"))
                add("implementation", libs.getLibrary("compose-foundation-layout"))
                add("implementation", libs.getLibrary("compose-material-iconsExtended"))
                add("implementation", libs.getLibrary("compose-material3"))
                add("implementation", libs.getLibrary("compose-ui"))
                add("implementation", libs.getLibrary("compose-ui-tooling-preview"))

                add("debugImplementation", libs.getLibrary("compose-ui-tooling"))
                add("debugImplementation", libs.getLibrary("compose-ui-testManifest"))
            }
        }
    }
}