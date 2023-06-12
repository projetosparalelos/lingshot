@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)

package com.teachmeprint.language.swipeable_permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.teachmeprint.language.R
import com.teachmeprint.language.swipeable_permission.SwipePermissionItem.DISPLAY_OVERLAY
import com.teachmeprint.language.swipeable_permission.SwipePermissionItem.INITIAL
import com.teachmeprint.language.swipeable_permission.SwipePermissionItem.READ_AND_WRITE
import com.teachmeprint.language.swipeable_permission.component.SwipePermissionAnimationIcon
import com.teachmeprint.language.swipeable_permission.util.PERMISSIONS
import com.teachmeprint.language.swipeable_permission.util.hasOverlayPermission
import kotlinx.coroutines.launch

@Composable
fun SwipePermissionRoute(
    context: Context = LocalContext.current,
    viewModel: SwipePermissionViewModel = hiltViewModel(),
    onNavigation: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val permissionState = rememberMultiplePermissionsState(PERMISSIONS)

    val launcherOverlayPermission =
        rememberLauncherForActivityResult(StartActivityForResult()) {
            if (hasOverlayPermission(context)) {
                onNavigation()
            }
        }

    val pagerState = rememberPagerState(
        initialPage = if (permissionState.allPermissionsGranted) {
            DISPLAY_OVERLAY.ordinal
        } else {
            INITIAL.ordinal
        }
    )

    SwipePermissionScreen(
        uiState = uiState,
        permissionState = permissionState,
        pagerState = pagerState,
        onOverlayPermission = {
            if (!hasOverlayPermission(context)) {
                launcherOverlayPermission.launch(
                    intentOverlayPermission()
                )
            }
        },
        onReadAndWritePermission = {
            context.startActivity(intentReadAndWritePermission(context))
        }
    )
}

@Composable
fun SwipePermissionScreen(
    permissionState: MultiplePermissionsState,
    uiState: SwipePermissionUiState,
    pagerState: PagerState,
    onOverlayPermission: () -> Unit,
    modifier: Modifier = Modifier,
    onReadAndWritePermission: () -> Unit
) {
    val scope = rememberCoroutineScope()

    HorizontalPager(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
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
                color = MaterialTheme.colorScheme.onSurface,
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
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Button(onClick = {
                when (item) {
                    INITIAL -> {
                        scope.launch {
                            pagerState.animateScrollToPage(READ_AND_WRITE.ordinal)
                        }
                    }

                    READ_AND_WRITE -> {
                        if (permissionState.shouldShowRationale) {
                            onReadAndWritePermission()
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }

                    DISPLAY_OVERLAY -> {
                        onOverlayPermission()
                    }
                }
            }) {
                Text(stringResource(id = R.string.text_button_swipe))
            }
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && pagerState.currentPage == READ_AND_WRITE.ordinal) {
            pagerState.animateScrollToPage(DISPLAY_OVERLAY.ordinal)
            return@LaunchedEffect
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwipePermissionScreenPreview() {
    SwipePermissionScreen(
        uiState = SwipePermissionUiState(),
        permissionState = rememberMultiplePermissionsState(permissions = PERMISSIONS),
        pagerState = rememberPagerState(),
        onOverlayPermission = {},
        onReadAndWritePermission = {}
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

private fun intentOverlayPermission() =
    Intent(ACTION_MANAGE_OVERLAY_PERMISSION)

private fun intentReadAndWritePermission(context: Context) =
    Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}")
    )