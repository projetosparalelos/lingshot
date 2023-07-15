package com.lingshot.language.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lingshot.home_presentation.navigation.homeScreen

@Composable
fun LingshotNavHost(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    NavHost(navController, startDestination = START_ROUTE) {
        startScreen(context) {
            navController.navigateToMain(
                navOptions {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            )
        }
        homeScreen()
    }
}
