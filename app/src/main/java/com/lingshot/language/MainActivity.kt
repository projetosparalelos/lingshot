package com.lingshot.language

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.language.presentation.ui.MainRoute
import com.lingshot.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import com.lingshot.swipepermission_presentation.ui.SwipePermissionRoute
import com.lingshot.swipepermission_presentation.util.allPermissionsGranted
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
            LingshotTheme {
                LingshotApp()
            }
        }
        screenCaptureFloatingWindowLifecycle(this)
    }

    @Composable
    private fun LingshotApp(
        systemUiController: SystemUiController = rememberSystemUiController(),
        statusBarColor: Color = MaterialTheme.colorScheme.surface
    ) {
        var reload by remember { mutableStateOf(false) }
        val allPermissionsGranted by remember(key1 = reload) {
            mutableStateOf(allPermissionsGranted(this))
        }

        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }

        if (allPermissionsGranted) {
            MainRoute()
        } else {
            SwipePermissionRoute(
                onUpPress = {
                    reload = !reload
                }
            )
        }
    }
}
