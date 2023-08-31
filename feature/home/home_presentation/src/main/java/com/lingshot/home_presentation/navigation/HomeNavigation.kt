@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.home_presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.lingshot.home_presentation.ui.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(homeDestination: HomeDestination) {
    composable(HOME_ROUTE) {
        HomeRoute(homeDestination = homeDestination)
    }
}
