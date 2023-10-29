@file:Suppress("UNUSED")

import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import extension.getLibrary
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                add("implementation", platform(libs.getLibrary("firebase-bom")))
                add("implementation", libs.getLibrary("firebase.analytics"))
                add("implementation", libs.getLibrary("firebase.crashlytics"))
                add("implementation", libs.getLibrary("firebase.performance"))
            }

            val extension = extensions.getByType<ApplicationExtension>()

            extension.apply {
                buildTypes.configureEach {
                    configure<CrashlyticsExtension> {
                        mappingFileUploadEnabled = false
                    }
                }
            }
        }
    }
}
