package com.teachmeprint.language.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teachmeprint.home_presentation.ui.HomeRoute
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionRoute
import com.teachmeprint.swipepermission_presentation.util.allPermissionsGranted

const val START_ROUTE = "start_route"

fun NavGraphBuilder.startScreen(context: Context, onUpPress: () -> Unit) {
    composable(START_ROUTE) {
        if (allPermissionsGranted(context)) {
            HomeRoute()
        } else {
            SwipePermissionRoute(onUpPress = onUpPress)
        }
    }
}