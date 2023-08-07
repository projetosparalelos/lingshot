@file:Suppress("UNUSED", "DEPRECATION")

import extension.getLibrary
import extension.libs
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.*

class AndroidQualityPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply {
                apply(DetektPlugin::class.java)
                apply(KtlintPlugin::class.java)
            }

            extensions.configure<DetektExtension> {
                config.setFrom("$rootDir/quality/detekt.yml")
            }

            extensions.configure<KtlintExtension> {
                android.set(true)
                ignoreFailures.set(false)
                disabledRules.set(
                    setOf(
                        "filename", "no-wildcard-imports",
                        "package-name", "final-newline", "max-line-length"
                    )
                )
                reporters {
                    reporter(PLAIN)
                    reporter(CHECKSTYLE)
                }
            }

            tasks.getByPath("preBuild").dependsOn("ktlintFormat")

            dependencies {
                add("detektPlugins", libs.getLibrary("compose-detekt"))
                add("lintChecks",libs.getLibrary("compose-lint-checks"))
            }
        }
    }
}
