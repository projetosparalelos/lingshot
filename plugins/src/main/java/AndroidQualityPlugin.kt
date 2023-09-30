@file:Suppress("UNUSED")

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import extension.getLibrary
import extension.libs
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidQualityPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {

            pluginManager.apply {
                apply(DetektPlugin::class.java)
                apply(SpotlessPlugin::class.java)
            }

            val editorConfig = mapOf(
                "android" to "true",
                "ij_kotlin_allow_trailing_comma" to "true",
                "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_no-wildcard-imports" to "disabled",
                "ktlint_standard_package-name" to "disabled"
            )

            fun FormatExtension.formatDefault(target: String) {
                targetExclude("**/build/**")
                target("**/src/**/$target")
                trimTrailingWhitespace()
                endWithNewline()
            }

            extensions.configure<SpotlessExtension> {
                kotlin {
                    formatDefault("*.kt")

                    ktlint().editorConfigOverride(editorConfig)
                    licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
                }

                kotlinGradle {
                    formatDefault("*.gradle.kts")

                    ktlint().editorConfigOverride(editorConfig)
                    licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
                }

                format("xml") {
                    formatDefault("*.xml")

                    // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
                    licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
                }
            }

            extensions.configure<DetektExtension> {
                config.setFrom("$rootDir/quality/detekt.yml")
            }

            tasks.getByPath("preBuild").dependsOn("spotlessApply")

            dependencies {
                add("detektPlugins", libs.getLibrary("compose-detekt"))
                add("lintChecks",libs.getLibrary("compose-lint-checks"))
            }
        }
    }
}
