package com.teachmeprint.language.presentation.screenshot.ui

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScreenShotActivity: ComponentActivity() {

    @Inject
    lateinit var screenShotFloatingWindow: ScreenShotFloatingWindow

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
            delay(1000L)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        screenShotFloatingWindow.showOrHide(false)
    }

    override fun onPause() {
        super.onPause()
        screenShotFloatingWindow.showOrHide()
    }
}