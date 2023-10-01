@file:Suppress("UNUSED")

import com.android.build.gradle.LibraryExtension
import extension.configureAndroidCompose
import helper.KeyHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidAdmobPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val extension = extensions.getByType<LibraryExtension>()

            extension.apply {
                configureAndroidCompose(extension)

                buildTypes {
                    release {
                        resValue("string", "ADMOB_APP_ID", KeyHelper.getValue("ADMOB_APP_ID"))
                        buildConfigField("String", "ADMOB_INTERSTITIAL_ID", KeyHelper.getValue("ADMOB_INTERSTITIAL_ID"))
                    }
                    debug {
                        resValue("string", "ADMOB_APP_ID", ADMOB_APP_ID_DEBUG)
                        buildConfigField("String", "ADMOB_INTERSTITIAL_ID", ADMOB_INTERSTITIAL_ID_DEBUG)
                    }
                }
            }
        }
    }

    companion object {
        private const val ADMOB_APP_ID_DEBUG = "\"ca-app-pub-3940256099942544~3347511713\""
        private const val ADMOB_INTERSTITIAL_ID_DEBUG = "\"ca-app-pub-3940256099942544/1033173712\""
    }
}
