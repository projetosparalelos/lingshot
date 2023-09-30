/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalAnimationApi::class)

package com.lingshot.completephrase_presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.lingshot.completephrase_presentation.ui.CompletePhraseScreenRoute

const val COMPLETE_PHRASE_ROUTE = "complete_phrase_route"
private const val LANGUAGE_ID = "languageId"
private const val LANGUAGE_FROM = "languageFrom"
private const val LANGUAGE_TO = "languageTo"

fun NavController.navigateToCompletePhrase(
    languageId: String,
    languageFrom: String,
    languageTo: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("$COMPLETE_PHRASE_ROUTE/$languageId/$languageFrom/$languageTo", navOptions)
}

fun NavGraphBuilder.completePhraseScreen(onBackClick: () -> Unit) {
    composable(
        route = "$COMPLETE_PHRASE_ROUTE/{$LANGUAGE_ID}/{$LANGUAGE_FROM}/{$LANGUAGE_TO}",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700),
            )
        },
    ) { navBackStackEntry ->
        val languageId = navBackStackEntry.arguments?.getString(LANGUAGE_ID).orEmpty()
        val languageFrom = navBackStackEntry.arguments?.getString(LANGUAGE_FROM).orEmpty()
        val languageTo = navBackStackEntry.arguments?.getString(LANGUAGE_TO).orEmpty()
        CompletePhraseScreenRoute(
            languageId = languageId,
            languageFrom = languageFrom,
            languageTo = languageTo,
            onBackClick = onBackClick,
        )
    }
}
