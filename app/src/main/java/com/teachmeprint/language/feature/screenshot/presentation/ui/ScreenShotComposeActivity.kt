package com.teachmeprint.language.feature.screenshot.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.teachmeprint.language.feature.screenshot.presentation.ui.screen.ScreenShotRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenShotComposeActivity: ComponentActivity() {
    private val imageUriPath by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenShotRoute(imageUri = imageUriPath)
        }
    }
}