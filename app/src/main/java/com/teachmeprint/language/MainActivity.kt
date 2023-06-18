package com.teachmeprint.language

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.language.navigation.TeachMePrintNavHost
import com.teachmeprint.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindowLifecycle: ScreenCaptureFloatingWindowLifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TeachMePrintTheme {
                TeachMePrintApp()
            }
        }
        screenCaptureFloatingWindowLifecycle(this)
    }

    @Composable
    private fun TeachMePrintApp(
        systemUiController: SystemUiController = rememberSystemUiController(),
        statusBarColor: Color = MaterialTheme.colorScheme.surface
    ) {
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }
        TeachMePrintNavHost()
    }
}