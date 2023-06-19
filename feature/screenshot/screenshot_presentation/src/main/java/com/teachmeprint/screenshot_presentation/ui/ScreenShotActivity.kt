package com.teachmeprint.screenshot_presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenShotActivity : ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindowLifecycle: ScreenCaptureFloatingWindowLifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TeachMePrintTheme {
                ScreenShotRoute()
            }
        }
        screenCaptureFloatingWindowLifecycle(this)
    }
}