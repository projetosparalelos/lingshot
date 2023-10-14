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
package com.lingshot.languagelearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.play.core.review.ReviewManagerFactory
import com.lingshot.common.helper.MainActivityManager.setMainActivity
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.languagelearn.presentation.ui.MainRoute
import com.lingshot.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindowLifecycle: ScreenCaptureFloatingWindowLifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {
            LingshotTheme {
                LingshotApp()
            }
        }
        setMainActivity(this)
        showFeedBackBottomSheetDialog()
        screenCaptureFloatingWindowLifecycle(this)
    }

    @Composable
    private fun LingshotApp(
        systemUiController: SystemUiController = rememberSystemUiController(),
        statusBarColor: Color = MaterialTheme.colorScheme.surface,
    ) {
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }
        MainRoute()
    }

    private fun showFeedBackBottomSheetDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener { review ->
            if (review.isSuccessful) {
                reviewManager.launchReviewFlow(this, review.result)
            }
        }
    }
}
