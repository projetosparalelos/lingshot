@file:OptIn(ExperimentalMaterial3Api::class)

package com.teachmeprint.language

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.designsystem.component.TeachMePrintOnLifecycleEvent
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.language.component.HomeToggleScreenCaptureButton
import com.teachmeprint.language.swipeable_permission.SwipePermissionRoute
import com.teachmeprint.language.swipeable_permission.util.allPermissionsGranted
import com.teachmeprint.language.swipeable_permission.util.isServiceRunning
import com.teachmeprint.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import com.teachmeprint.screencapture.service.ScreenShotService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {

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
                if (allPermissionsGranted(this@MainComposeActivity)) {
                    HomeScreen()
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
                HomeScreen()
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

    @Composable
    fun HomeScreen() {
        var enablePermission by remember { mutableStateOf(false) }
        val launcherScreenShotService =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    startScreenShotService(result.data)
                }
            }

        TeachMePrintOnLifecycleEvent { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (isServiceRunning(this) != enablePermission) {
                        enablePermission = !enablePermission
                    }
                }
                else -> {}
            }
        }

        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.app_name)
                        )
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Rounded.Settings, contentDescription = null)
                        }
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                HomeToggleScreenCaptureButton(
                    permissionsGranted = enablePermission,
                    onPermissions = {
                        if (enablePermission) {
                            stopScreenShotService()
                            enablePermission = !enablePermission
                        } else {
                            launcherScreenShotService.launch(mediaProjectionIntent())
                        }
                    },
                    onFinishActivity = {
                        finish()
                    }
                )
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        )
    }

    private fun startScreenShotService(resultData: Intent?) {
        ScreenShotService.getStartIntent(this, resultData).also {
            startService(it)
        }
    }

    private fun stopScreenShotService() {
        ScreenShotService.getStopIntent(this).also {
            stopService(it)
        }
    }
    private fun mediaProjectionIntent() =
        (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
            .createScreenCaptureIntent()
}