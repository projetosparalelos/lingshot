package com.teachmeprint.language.feature.screenshot.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.teachmeprint.language.feature.screenshot.presentation.ui.screen.ScreenShotRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenShotComposeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenShotRoute()
        }
    }
}