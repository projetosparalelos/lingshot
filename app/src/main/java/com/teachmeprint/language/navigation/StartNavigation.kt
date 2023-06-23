package com.teachmeprint.language.navigation

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teachmeprint.language.presentation.ui.MainRoute
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionRoute
import com.teachmeprint.swipepermission_presentation.util.allPermissionsGranted

const val START_ROUTE = "start_route"

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    this.navigate(START_ROUTE, navOptions)
}

fun NavGraphBuilder.startScreen(context: Context, onUpPress: () -> Unit) {
    composable(START_ROUTE) {
        if (allPermissionsGranted(context)) {
            MainRoute()
        } else {
            SwipePermissionRoute(onUpPress = onUpPress)
        }
    }
}