@file:Suppress("UNUSED")

import com.android.build.gradle.LibraryExtension
import helper.KeyHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidNetworkPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply {
                apply("kotlinx-serialization")
            }

            val extension = extensions.getByType<LibraryExtension>()

            extension.apply {
                defaultConfig {
                    buildConfigField("String", "BASE_API", "\"https://api.openai.com/v1/\"")
                }

                defaultConfig {
                    buildConfigField("String", "GOOGLE_AUTH_ID", KeyHelper.getValue("GOOGLE_AUTH_ID"))
                }

                buildTypes {
                    release {
                        buildConfigField(
                            "String",
                            "CHAT_GPT_KEY",
                            KeyHelper.getValue("CHAT_GPT_KEY_RELEASE")
                        )
                    }
                    debug {
                        buildConfigField(
                            "String",
                            "CHAT_GPT_KEY",
                            KeyHelper.getValue("CHAT_GPT_KEY_DEBUG")
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val BASE_API: String = "https://api.openai.com/v1/"
    }
}