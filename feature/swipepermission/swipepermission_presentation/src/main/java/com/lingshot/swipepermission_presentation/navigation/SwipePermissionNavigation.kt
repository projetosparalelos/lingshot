@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.swipepermission_presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.lingshot.swipepermission_presentation.ui.SwipePermissionRoute

const val SWIPE_PERMISSION_ROUTE = "swipe_permission_route"

fun NavController.navigateToSwipePermission(navOptions: NavOptions? = null) {
    this.navigate(SWIPE_PERMISSION_ROUTE, navOptions)
}

fun NavGraphBuilder.swipePermissionScreen(onNavigateToHome: () -> Unit) {
    composable(
        route = SWIPE_PERMISSION_ROUTE
    ) {
        SwipePermissionRoute(onNavigateToHome = onNavigateToHome)
    }
}
