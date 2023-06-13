package com.teachmeprint.language

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.home_presentation.ui.HomeRoute
import com.teachmeprint.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionRoute
import com.teachmeprint.swipepermission_presentation.util.allPermissionsGranted
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
            StatusBarColor()
            TeachMePrintTheme {
                AppNavigation()
            }
        }
        screenCaptureFloatingWindowLifecycle(this)
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "start") {
            composable("start") {
                if (allPermissionsGranted(this@MainActivity)) {
                    HomeRoute()
                } else {
                    SwipePermissionRoute {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            composable("home") {
                HomeRoute()
            }
        }
    }

    @Composable
    private fun StatusBarColor(color: Color = MaterialTheme.colorScheme.surface) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(color)
        }
    }
}