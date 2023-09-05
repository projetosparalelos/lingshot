@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalLayoutApi::class
)

package com.lingshot.language.presentation.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Context
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lingshot.common.util.findActivity
import com.lingshot.designsystem.component.LingshotOnLifecycleEvent
import com.lingshot.designsystem.theme.LocalSchemeCustom
import com.lingshot.designsystem.theme.SchemeCustom
import com.lingshot.home_presentation.navigation.HOME_ROUTE
import com.lingshot.home_presentation.ui.component.HomeToggleServiceButton
import com.lingshot.language.R
import com.lingshot.language.navigation.LingshotAppState
import com.lingshot.language.navigation.LingshotNavHost
import com.lingshot.language.navigation.rememberLingshotAppState
import com.lingshot.language.presentation.MainEvent
import com.lingshot.language.presentation.MainUiState
import com.lingshot.language.presentation.MainViewModel
import com.lingshot.screencapture.service.ScreenShotService.Companion.getStartIntent
import com.lingshot.screencapture.service.ScreenShotService.Companion.getStopIntent
import com.lingshot.screencapture.util.isServiceRunning
import com.lingshot.swipepermission_presentation.ui.intentApplicationDetailsPermission
import com.lingshot.swipepermission_presentation.util.allPermissionsGranted
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.skydoves.balloon.compose.setOverlayColor
import com.skydoves.balloon.compose.setTextColor
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect

@Composable
fun MainRoute(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSignInSuccessful by viewModel.isSignInSuccessful.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        isSignInSuccessful = isSignInSuccessful,
        handleEvent = viewModel::handleEvent
    )
}

@Composable
private fun MainScreen(
    uiState: MainUiState,
    isSignInSuccessful: Boolean,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    lingshotAppState: LingshotAppState = rememberLingshotAppState(),
    handleEvent: (MainEvent) -> Unit
) {
    val activity = context.findActivity()

    val shouldShowHomeScreen =
        lingshotAppState.currentDestination?.route == HOME_ROUTE

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(POST_NOTIFICATIONS)
    } else {
        null
    }

    val launcherScreenShotService =
        rememberLauncherForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                activity?.startScreenShotService(result.data)
            }
        }

    LingshotOnLifecycleEvent { _, event ->
        when (event) {
            ON_RESUME -> {
                if (isServiceRunning(context) != uiState.isServiceRunning) {
                    handleEvent(MainEvent.ToggleServiceButton)
                }
            }

            else -> Unit
        }
    }

    val allPermissionsAndIsSignInGranted by remember {
        mutableStateOf(allPermissionsGranted(context) && isSignInSuccessful)
    }

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            AnimatedVisibility(
                visible = shouldShowHomeScreen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val textMessageNotificationPermission = stringResource(
                    id = R.string.text_message_notification_permission
                )
                Balloon(builder = rememberBalloonBuilder()) { balloonWindow ->
                    HomeToggleServiceButton(
                        isServiceRunning = uiState.isServiceRunning,
                        onToggleServiceButton = {
                            if (notificationPermissionState?.status?.isGranted?.not() == true) {
                                if (notificationPermissionState.status.shouldShowRationale) {
                                    context.startActivity(
                                        intentApplicationDetailsPermission(context)
                                    )
                                    makeText(
                                        context,
                                        textMessageNotificationPermission,
                                        LENGTH_LONG
                                    ).show()
                                } else {
                                    notificationPermissionState.launchPermissionRequest()
                                }
                                return@HomeToggleServiceButton
                            }

                            if (uiState.isServiceRunning) {
                                activity?.stopScreenShotService()
                                handleEvent(MainEvent.ToggleServiceButton)
                            } else {
                                launcherScreenShotService.launch(activity?.mediaProjectionIntent())
                            }
                        },
                        onFinishActivity = {
                            activity?.finish()
                        }
                    )
                    if (uiState.isBalloonOverlayVisible) {
                        balloonWindow.apply {
                            showAlignTop()
                            setOnBalloonDismissListener {
                                handleEvent(MainEvent.HideBalloonOverlay)
                            }
                        }
                    }
                }
            }
        },
        content = { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                LingshotNavHost(
                    navController = lingshotAppState.navController,
                    allPermissionsAndIsSignInGranted = allPermissionsAndIsSignInGranted
                )
            }
        }
    )
}

@Composable
private fun rememberBalloonBuilder(
    context: Context = LocalContext.current,
    colorSchemeCustom: SchemeCustom = LocalSchemeCustom.current,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) = rememberBalloonBuilder {
    setText(context.getString(R.string.text_message_balloon_overlay_screenshot))
    setTextSize(14f)
    setWidthRatio(1.0f)
    setHeight(BalloonSizeSpec.WRAP)
    setArrowOrientation(ArrowOrientation.BOTTOM)
    setPadding(12)
    setMarginHorizontal(16)
    setCornerRadius(24f)
    setIsVisibleOverlay(true)
    setOverlayShape(BalloonOverlayRoundRect(52f, 52f))
    setBalloonHighlightAnimation(BalloonHighlightAnimation.HEARTBEAT)
    setOverlayColor(colorSchemeCustom.overlay)
    setTextColor(textColor)
    setBackgroundColor(containerColor)
}

private fun Activity.startScreenShotService(resultData: Intent?) =
    getStartIntent(this, resultData).also {
        startService(it)
    }

private fun Activity.stopScreenShotService() =
    getStopIntent(this).also {
        stopService(it)
    }

private fun Activity.mediaProjectionIntent() =
    (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
        .createScreenCaptureIntent()
