@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.language.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.lingshot.completephrase_presentation.navigation.completePhraseScreen
import com.lingshot.completephrase_presentation.navigation.navigateToCompletePhrase
import com.lingshot.home_presentation.ui.navigation.HOME_ROUTE
import com.lingshot.home_presentation.ui.navigation.HomeDestination
import com.lingshot.home_presentation.ui.navigation.homeScreen

@Composable
fun LingshotNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        val homeDestination = homeDestination(navController)

        homeScreen(homeDestination)
        completePhraseScreen()
    }
}

private val homeDestination: (NavHostController) -> HomeDestination = { nav ->
    HomeDestination(
        onNavigateToCompletePhrase = { languageId ->
            nav.navigateToCompletePhrase(languageId)
        }
    )
}
