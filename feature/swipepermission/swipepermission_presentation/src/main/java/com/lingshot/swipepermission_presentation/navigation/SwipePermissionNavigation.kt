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
        route = SWIPE_PERMISSION_ROUTE,
    ) {
        SwipePermissionRoute(onNavigateToHome = onNavigateToHome)
    }
}
