@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.completephrase_presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.lingshot.completephrase_presentation.ui.CompletePhraseScreenRoute

const val COMPLETE_PHRASE_ROUTE = "complete_phrase_route"

fun NavController.navigateToCompletePhrase(navOptions: NavOptions? = null) {
    this.navigate(COMPLETE_PHRASE_ROUTE, navOptions)
}

fun NavGraphBuilder.completePhraseScreen() {
    composable(
        route = COMPLETE_PHRASE_ROUTE,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) {
        CompletePhraseScreenRoute()
    }
}
