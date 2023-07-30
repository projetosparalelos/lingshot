@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lingshot.common.util.findActivity
import com.lingshot.designsystem.component.LingshotOnLifecycleEvent
import com.lingshot.home_presentation.ui.HomeRoute
import com.lingshot.home_presentation.ui.component.HomeToggleServiceButton
import com.lingshot.language.R
import com.lingshot.language.presentation.MainEvent
import com.lingshot.language.presentation.MainUiState
import com.lingshot.language.presentation.MainViewModel
import com.lingshot.screencapture.service.ScreenShotService.Companion.getStartIntent
import com.lingshot.screencapture.service.ScreenShotService.Companion.getStopIntent
import com.lingshot.screencapture.util.isServiceRunning
import com.lingshot.swipepermission_presentation.ui.intentApplicationDetailsPermission

@Composable
fun MainRoute(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        handleEvent = viewModel::handleEvent
    )
}

@Composable
private fun MainScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    handleEvent: (MainEvent) -> Unit
) {
    val activity = context.findActivity()
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
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hi, ${uiState.userDomain?.firstName}",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        AsyncImage(
                            modifier = Modifier.clip(CircleShape),
                            model = uiState.userDomain?.profilePictureUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
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
            val textMessageNotificationPermission = stringResource(
                id = R.string.text_message_notification_permission
            )
            HomeToggleServiceButton(
                isServiceRunning = uiState.isServiceRunning,
                onToggleServiceButton = {
                    if (notificationPermissionState?.status?.isGranted?.not() == true) {
                        if (notificationPermissionState.status.shouldShowRationale) {
                            context.startActivity(intentApplicationDetailsPermission(context))
                            makeText(context, textMessageNotificationPermission, LENGTH_LONG).show()
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
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                HomeRoute()
            }
        }
    )
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
