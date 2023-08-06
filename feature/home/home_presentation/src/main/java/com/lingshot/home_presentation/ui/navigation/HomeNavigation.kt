@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.home_presentation.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.lingshot.home_presentation.ui.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavGraphBuilder.homeScreen(homeDestination: HomeDestination) {
    composable(HOME_ROUTE) {
        HomeRoute(homeDestination = homeDestination)
    }
}
