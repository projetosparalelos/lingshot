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
private const val LANGUAGE_ID = "languageId"

fun NavController.navigateToCompletePhrase(languageId: String, navOptions: NavOptions? = null) {
    this.navigate("$COMPLETE_PHRASE_ROUTE/$languageId", navOptions)
}

fun NavGraphBuilder.completePhraseScreen(onBackClick: () -> Unit) {
    composable(
        route = "$COMPLETE_PHRASE_ROUTE/{$LANGUAGE_ID}",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) { navBackStackEntry ->
        val languageId = navBackStackEntry.arguments?.getString(LANGUAGE_ID)
        CompletePhraseScreenRoute(languageId = languageId, onBackClick = onBackClick)
    }
}
