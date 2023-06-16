pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "TeachMePrint"
include(":app")
include(":feature:screenshot:screenshot_domain")
include(":feature:screenshot:screenshot_data")
include(":feature:screenshot:screenshot_presentation")
include(":feature:screencapture")
include(":feature:languagechoice:languagechoice_domain")
include(":feature:languagechoice:languagechoice_data")
include(":feature:languagechoice:languagechoice_presentation")
include(":core:common")
include(":core:designsystem")
include(":data:remote")
include(":data:local")
include(":core:navigation")
include(":domain")
include(":feature:home:home_presentation")
include(":feature:swipepermission:swipepermission_presentation")
