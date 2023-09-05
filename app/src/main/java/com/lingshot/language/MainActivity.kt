package com.lingshot.language

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.language.presentation.MainViewModel
import com.lingshot.language.presentation.ui.MainRoute
import com.lingshot.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var screenCaptureFloatingWindowLifecycle: ScreenCaptureFloatingWindowLifecycle
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        var isSignInSuccessful by mutableStateOf(false)

        lifecycleScope.launch {
            viewModel.isSignInSuccessful
                .onEach {
                    isSignInSuccessful = it
                }
                .collect()
        }

        installSplashScreen().apply {
            if (isSignInSuccessful) {
                setKeepOnScreenCondition { false }
            }
        }

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
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }
        MainRoute(viewModel = viewModel)
    }
}
