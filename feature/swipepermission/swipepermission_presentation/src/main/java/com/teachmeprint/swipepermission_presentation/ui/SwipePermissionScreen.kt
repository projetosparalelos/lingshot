@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)

package com.teachmeprint.swipepermission_presentation.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.teachmeprint.swipepermission_presentation.R
import com.teachmeprint.swipepermission_presentation.SwipePermissionEvent
import com.teachmeprint.swipepermission_presentation.SwipePermissionEvent.ClearState
import com.teachmeprint.swipepermission_presentation.SwipePermissionEvent.SignInWithIntent
import com.teachmeprint.swipepermission_presentation.SwipePermissionUiState
import com.teachmeprint.swipepermission_presentation.SwipePermissionViewModel
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionItem.DISPLAY_OVERLAY
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionItem.INITIAL
import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionItem.READ_AND_WRITE
import com.teachmeprint.swipepermission_presentation.ui.component.SwipePermissionAnimationIcon
import com.teachmeprint.swipepermission_presentation.ui.component.SwipePermissionGoogleAuthButton
import com.teachmeprint.swipepermission_presentation.util.PERMISSIONS
import com.teachmeprint.swipepermission_presentation.util.hasOverlayPermission
import kotlinx.coroutines.launch

@Composable
fun SwipePermissionRoute(
    viewModel: SwipePermissionViewModel = hiltViewModel(),
    onUpPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SwipePermissionScreen(
        uiState = uiState,
        onSignIn = viewModel::signIn,
        handleEvent = viewModel::handleEvent,
        onUpPress = onUpPress
    )
}

@Composable
private fun SwipePermissionScreen(
    uiState: SwipePermissionUiState,
    onSignIn: suspend () -> IntentSender?,
    handleEvent: (SwipePermissionEvent) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onUpPress: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(PERMISSIONS)
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = when {
            uiState.isSignInSuccessful -> READ_AND_WRITE.ordinal
            permissionState.allPermissionsGranted -> DISPLAY_OVERLAY.ordinal
            else -> INITIAL.ordinal
        }
    )

    val launcherOverlayPermission =
        rememberLauncherForActivityResult(StartActivityForResult()) {
            if (hasOverlayPermission(context)) {
                onUpPress()
            }
        }

    val launcherSignIn = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            handleEvent(SignInWithIntent(result.data))
        }
    }

    Surface {
        HorizontalPager(
            modifier = modifier,
            pageCount = uiState.swipePermissionItemList.size,
            state = pagerState,
            userScrollEnabled = false,
            key = { uiState.swipePermissionItemList[it] }
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                val item = uiState.swipePermissionItemList[index]

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = item.title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                SwipePermissionAnimationIcon(icon = item.icon)

                val text = if (!permissionState.shouldShowRationale) {
                    item.text
                } else {
                    item.secondText ?: item.text
                }

                Text(
                    text = stringResource(id = text),
                    textAlign = TextAlign.Center
                )

                if (item == INITIAL) {
                    SwipePermissionGoogleAuthButton(
                        onSignIn = {
                            scope.launch {
                                launcherSignIn.launch(
                                    intentSignIn(onSignIn() ?: return@launch)
                                )
                            }
                        }
                    )
                } else {
                    Button(onClick = {
                        when (item) {
                            READ_AND_WRITE -> {
                                if (permissionState.shouldShowRationale) {
                                    context.startActivity(intentReadAndWritePermission(context))
                                } else {
                                    permissionState.launchMultiplePermissionRequest()
                                }
                            }

                            DISPLAY_OVERLAY -> {
                                if (!hasOverlayPermission(context)) {
                                    launcherOverlayPermission.launch(
                                        intentOverlayPermission()
                                    )
                                } else {
                                    onUpPress()
                                }
                            }

                            else -> Unit
                        }
                    }) {
                        Text(stringResource(id = R.string.text_button_swipe))
                    }
                }
            }
        }
    }

    LaunchedEffect(
        key1 = permissionState.allPermissionsGranted,
        key2 = uiState.isSignInSuccessful,
        key3 = uiState.signInError
    ) {
        if (permissionState.allPermissionsGranted && pagerState.currentPage == READ_AND_WRITE.ordinal) {
            pagerState.animateScrollToPage(DISPLAY_OVERLAY.ordinal)
            return@LaunchedEffect
        }

        if (uiState.isSignInSuccessful) {
            pagerState.animateScrollToPage(READ_AND_WRITE.ordinal)
        }

        uiState.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            handleEvent(ClearState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipePermissionScreenPreview() {
    SwipePermissionScreen(
        uiState = SwipePermissionUiState(),
        onSignIn = { null },
        handleEvent = {},
        onUpPress = {}
    )
}

enum class SwipePermissionItem(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @StringRes val secondText: Int? = null,
    @RawRes val icon: Int
) {
    INITIAL(
        title = R.string.text_title_swipe_initial,
        text = R.string.text_message_swipe_initial,
        icon = R.raw.swipe_initial_animation
    ),
    READ_AND_WRITE(
        title = R.string.text_title_swipe_read_and_write,
        text = R.string.text_message_swipe_read_and_write,
        secondText = R.string.text_second_message_swipe_read_and_write,
        icon = R.raw.swipe_read_and_write_animation
    ),
    DISPLAY_OVERLAY(
        title = R.string.text_title_swipe_overlay,
        text = R.string.text_message_swipe_overlay,
        icon = R.raw.swipe_overlay_animation
    );
}

private fun intentSignIn(intentSender: IntentSender) =
    IntentSenderRequest.Builder(intentSender).build()

private fun intentOverlayPermission() =
    Intent(ACTION_MANAGE_OVERLAY_PERMISSION)

private fun intentReadAndWritePermission(context: Context) =
    Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}")
    )