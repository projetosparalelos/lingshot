@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("plugins")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://storage.googleapis.com/r8-releases/raw")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://www.jitpack.io")
    }
}
rootProject.name = "Lingshot"
include(":app")
include(":core:common")
include(":core:designsystem")
include(":data:local")
include(":data:remote")
include(":domain")
include(":feature:completephrase:completephrase_presentation")
include(":feature:home:home_domain")
include(":feature:home:home_presentation")
include(":feature:languagechoice:languagechoice_data")
include(":feature:languagechoice:languagechoice_domain")
include(":feature:languagechoice:languagechoice_presentation")
include(":feature:phrasemaster:phrasemaster_data")
include(":feature:phrasemaster:phrasemaster_domain")
include(":feature:phrasemaster:phrasemaster_presentation")
include(":feature:reviewlevel:reviewlevel_domain")
include(":feature:screencapture")
include(":feature:screenshot:screenshot_domain")
include(":feature:screenshot:screenshot_presentation")
include(":feature:swipepermission:swipepermission_presentation")
