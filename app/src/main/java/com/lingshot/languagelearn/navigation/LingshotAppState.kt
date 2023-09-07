@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.languagelearn.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun rememberLingshotAppState(
    navController: NavHostController = rememberAnimatedNavController()
): LingshotAppState {
    return remember {
        LingshotAppState(navController = navController)
    }
}

@Stable
data class LingshotAppState(val navController: NavHostController) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
}
