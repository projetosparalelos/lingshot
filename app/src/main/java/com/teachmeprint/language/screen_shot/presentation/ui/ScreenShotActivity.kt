package com.teachmeprint.language.screen_shot.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.language.screen_capture.presentation.ui.ScreenCaptureFloatingWindow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class ScreenShotActivity: ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindow: ScreenCaptureFloatingWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContent {
            ScreenShotStatusBarColor()
            ScreenShotRoute()
        }
    }

    @Composable
    private fun ScreenShotStatusBarColor() {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(Color.Black)
        }
    }

    private fun hideSystemUI() {
        lifecycleScope.launch {
            delay(1.seconds)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        screenCaptureFloatingWindow.showOrHide(false)
    }

    override fun onPause() {
        super.onPause()
        screenCaptureFloatingWindow.showOrHide()
    }
}