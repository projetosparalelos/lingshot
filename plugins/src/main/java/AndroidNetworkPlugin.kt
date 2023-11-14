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
                    buildConfigField("String", "BASE_CHAT_GPT_API", BASE_CHAT_GPT_API)
                    buildConfigField("String", "BASE_GOOGLE_TRANSLATE_API", BASE_GOOGLE_TRANSLATE_API)
                    buildConfigField("String", "CHAT_GPT_KEY", KeyHelper.getValue("CHAT_GPT_KEY"))
                    buildConfigField("String", "GOOGLE_TRANSLATE_KEY", KeyHelper.getValue("GOOGLE_TRANSLATE_KEY"))
                }
            }
        }
    }

    companion object {
        private const val BASE_CHAT_GPT_API: String = "\"https://api.openai.com/v1/\""
        private const val BASE_GOOGLE_TRANSLATE_API: String = "\"https://translation.googleapis.com/\""
    }
}
