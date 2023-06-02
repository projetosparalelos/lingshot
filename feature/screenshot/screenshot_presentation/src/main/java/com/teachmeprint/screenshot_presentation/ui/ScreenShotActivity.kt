package com.teachmeprint.screenshot_presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.screencapture.ScreenCaptureFloatingWindow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenShotActivity : ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindow: ScreenCaptureFloatingWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onResume() {
        super.onResume()
        screenCaptureFloatingWindow.showOrHide(false)
    }

    override fun onPause() {
        super.onPause()
        screenCaptureFloatingWindow.showOrHide()
    }
}