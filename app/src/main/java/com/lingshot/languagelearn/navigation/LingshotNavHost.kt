@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.languagelearn.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.lingshot.completephrase_presentation.navigation.completePhraseScreen
import com.lingshot.completephrase_presentation.navigation.navigateToCompletePhrase
import com.lingshot.home_presentation.navigation.HOME_ROUTE
import com.lingshot.home_presentation.navigation.HomeDestination
import com.lingshot.home_presentation.navigation.homeScreen
import com.lingshot.home_presentation.navigation.navigateToHome
import com.lingshot.swipepermission_presentation.navigation.SWIPE_PERMISSION_ROUTE
import com.lingshot.swipepermission_presentation.navigation.navigateToSwipePermission
import com.lingshot.swipepermission_presentation.navigation.swipePermissionScreen

@Composable
fun LingshotNavHost(
    navController: NavHostController,
    allPermissionsAndIsSignInGranted: Boolean
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = if (allPermissionsAndIsSignInGranted) {
            HOME_ROUTE
        } else {
            SWIPE_PERMISSION_ROUTE
        }
    ) {
        val homeDestination = homeDestination(navController)

        swipePermissionScreen {
            navController.navigateToHome(
                navOptions = navOptions { popUpToTop(navController) }
            )
        }
        homeScreen(homeDestination)
        completePhraseScreen(onBackClick = navController::popBackStack)
    }
}

private val homeDestination: (NavHostController) -> HomeDestination = { nav ->
    HomeDestination(
        onSignOut = {
            nav.navigateToSwipePermission(
                navOptions = navOptions { popUpToTop(nav) }
            )
        },
        onNavigateToCompletePhrase = { languageId, languageFrom, languageTo ->
            nav.navigateToCompletePhrase(languageId, languageFrom, languageTo)
        }
    )
}

private fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}
