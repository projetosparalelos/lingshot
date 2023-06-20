@file:OptIn(ExperimentalMaterial3Api::class)

package com.teachmeprint.home_presentation.ui

import android.app.Activity
import android.content.Context
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.activity.ComponentActivity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teachmeprint.common.R.*
import com.teachmeprint.common.util.findActivity
import com.teachmeprint.designsystem.component.TeachMePrintOnLifecycleEvent
import com.teachmeprint.home_presentation.HomeEvent
import com.teachmeprint.home_presentation.HomeScreen2
import com.teachmeprint.home_presentation.HomeUiState
import com.teachmeprint.home_presentation.HomeViewModel
import com.teachmeprint.home_presentation.ui.component.HomeToggleServiceButton
import com.teachmeprint.screencapture.service.ScreenShotService.Companion.getStartIntent
import com.teachmeprint.screencapture.service.ScreenShotService.Companion.getStopIntent
import com.teachmeprint.screencapture.util.isServiceRunning

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        handleEvent = viewModel::handleEvent
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    handleEvent: (HomeEvent) -> Unit
) {
    val activity = context.findActivity()

    val launcherScreenShotService =
        rememberLauncherForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                activity?.startScreenShotService(result.data)
            }
        }

    TeachMePrintOnLifecycleEvent { _, event ->
        when (event) {
            ON_RESUME -> {
                if (isServiceRunning(context) != uiState.isServiceRunning) {
                    handleEvent(HomeEvent.ToggleServiceButton)
                }
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(id = string.app_name)
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
            HomeToggleServiceButton(
                isServiceRunning = uiState.isServiceRunning,
                onToggleServiceButton = {
                    if (uiState.isServiceRunning) {
                        activity?.stopScreenShotService()
                        handleEvent(HomeEvent.ToggleServiceButton)
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
                HomeScreen2()
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