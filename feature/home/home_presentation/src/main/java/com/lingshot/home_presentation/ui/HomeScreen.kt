/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalPermissionsApi::class)
@file:Suppress("TooManyFunctions")

package com.lingshot.home_presentation.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lingshot.common.util.findActivity
import com.lingshot.designsystem.component.LingshotOnLifecycleEvent
import com.lingshot.home_domain.model.HomeTypeSection
import com.lingshot.home_domain.model.TypeActionScreenshot
import com.lingshot.home_domain.model.TypeActionScreenshot.DEVICE_BUTTON
import com.lingshot.home_domain.model.TypeActionScreenshot.FLOATING_BALLOON
import com.lingshot.home_domain.model.screenShotActions
import com.lingshot.home_presentation.HomeEvent
import com.lingshot.home_presentation.HomeEvent.SaveLanguage
import com.lingshot.home_presentation.HomeEvent.SelectedOptionsLanguage
import com.lingshot.home_presentation.HomeEvent.ToggleLanguageDialog
import com.lingshot.home_presentation.HomeEvent.ToggleServiceButton
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.HomeViewModel
import com.lingshot.home_presentation.R
import com.lingshot.home_presentation.navigation.HomeDestination
import com.lingshot.home_presentation.ui.component.HomeLanguageChoice
import com.lingshot.home_presentation.ui.component.HomeOptionScreenShotCard
import com.lingshot.home_presentation.ui.component.HomeSubtitleCard
import com.lingshot.home_presentation.ui.component.HomeToolbar
import com.lingshot.languagechoice_domain.model.TranslateLanguageType
import com.lingshot.languagechoice_presentation.ui.LanguageChoiceDialog
import com.lingshot.screencapture.service.ScreenShotService.Companion.isScreenCaptureByDevice
import com.lingshot.screencapture.service.ScreenShotService.Companion.isScreenCaptureForSubtitle
import com.lingshot.screencapture.service.ScreenShotService.Companion.screenShotServiceIntent
import com.lingshot.screencapture.service.ScreenShotService.Companion.screenShotServiceIntentWithMediaProjection
import com.lingshot.screencapture.util.isServiceRunning
import es.dmoral.toasty.Toasty.warning
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeRoute(
    homeDestination: HomeDestination,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        homeDestination = homeDestination,
        handleEvent = viewModel::handleEvent,
        uiState = uiState,
    )
}

@Composable
internal fun HomeScreen(
    homeDestination: HomeDestination,
    handleEvent: (HomeEvent) -> Unit,
    uiState: HomeUiState,
    context: Context = LocalContext.current,
) {
    val activity = context.findActivity()

    var enabledType by remember {
        mutableStateOf(getInitialEnabledType())
    }

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    val launcherScreenShotService =
        rememberLauncherForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                activity?.startScreenShotServiceWithResult(result.data)
            }
        }

    LingshotOnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (isServiceRunning(context) != uiState.isServiceRunning) {
                    handleEvent(ToggleServiceButton)
                }
            }

            else -> Unit
        }
    }
    val textMessageNotificationPermission = stringResource(
        id = R.string.text_message_notification_permission_home,
    )
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HomeToolbar(homeDestination)

            LazyColumn(
                contentPadding = PaddingValues(all = 16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(uiState.homeSection) { section ->
                    when (section.typeSection) {
                        HomeTypeSection.CARD_LANGUAGE_CHOICE -> {
                            HomeLanguageChoice(
                                languageFrom = uiState.languageFrom,
                                languageTo = uiState.languageTo,
                                onClickLanguageFrom = {
                                    handleEvent(ToggleLanguageDialog(TranslateLanguageType.FROM))
                                },
                                onClickLanguageTo = {
                                    handleEvent(ToggleLanguageDialog(TranslateLanguageType.TO))
                                },
                            )
                        }

                        HomeTypeSection.CARD_SUBTITLE -> {
                            HomeSubtitleCard(
                                isEnabled = if (uiState.isServiceRunning) {
                                    isScreenCaptureForSubtitle
                                } else {
                                    true
                                },
                                isSelected = (isScreenCaptureForSubtitle && uiState.isServiceRunning),
                                onClickChanged = {
                                    if (notificationPermissionState?.status?.isGranted?.not() == true) {
                                        if (notificationPermissionState.status.shouldShowRationale) {
                                            handleNotificationPermission(
                                                textMessage = textMessageNotificationPermission,
                                                context = context,
                                            )
                                        } else {
                                            notificationPermissionState.launchPermissionRequest()
                                        }
                                        return@HomeSubtitleCard
                                    }

                                    activity?.handleServiceToggle(
                                        isServiceRunning = uiState.isServiceRunning,
                                        handleEvent = handleEvent,
                                        launchScreenShot = {
                                            launcherScreenShotService.launch(it.mediaProjectionIntent())
                                        },
                                    )
                                    isScreenCaptureForSubtitle = true
                                },
                            )
                        }

                        HomeTypeSection.CARD_BUTTON_SCREEN_SHOT -> {
                            screenShotActions.filter {
                                it.typeActionScreenshot.isUpsideDownCakeAndNotDeviceButton()
                            }.fastForEachIndexed { position, item ->
                                val isCheckForItem = item.typeActionScreenshot == enabledType
                                HomeOptionScreenShotCard(
                                    onClickChanged = {
                                        if (notificationPermissionState?.status?.isGranted?.not() == true) {
                                            if (notificationPermissionState.status.shouldShowRationale) {
                                                handleNotificationPermission(
                                                    textMessage = textMessageNotificationPermission,
                                                    context = context,
                                                )
                                            } else {
                                                notificationPermissionState.launchPermissionRequest()
                                            }
                                            return@HomeOptionScreenShotCard
                                        }

                                        activity?.handleServiceToggle(
                                            isServiceRunning = uiState.isServiceRunning,
                                            handleEvent = handleEvent,
                                            launchScreenShot = {
                                                if (item.typeActionScreenshot == FLOATING_BALLOON) {
                                                    launcherScreenShotService.launch(it.mediaProjectionIntent())
                                                } else {
                                                    it.startScreenShotService()
                                                    handleEvent(ToggleServiceButton)
                                                }
                                            },
                                        )
                                        enabledType = item.typeActionScreenshot
                                        isScreenCaptureForSubtitle = false
                                    },
                                    icon = if (item.typeActionScreenshot == FLOATING_BALLOON) {
                                        Icons.Default.FitScreen
                                    } else {
                                        Icons.Default.Screenshot
                                    },
                                    imageOnboarding = if (item.typeActionScreenshot == FLOATING_BALLOON) {
                                        R.drawable.floating_balloon_onboarding
                                    } else {
                                        R.drawable.device_button_onboarding
                                    },

                                    title = item.title,
                                    description = item.description,
                                    isEnabled = if (uiState.isServiceRunning) {
                                        if (isScreenCaptureForSubtitle && isCheckForItem) {
                                            false
                                        } else {
                                            isCheckForItem
                                        }
                                    } else {
                                        true
                                    },
                                    isChecked = (isCheckForItem && isScreenCaptureForSubtitle.not() && uiState.isServiceRunning),
                                )
                                if (position != screenShotActions.size.minus(1)) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (uiState.isLanguageDialogVisible) {
        LanguageChoiceDialog(
            availableLanguage = uiState.selectedOptionsLanguage,
            availableLanguageList = uiState.availableLanguageList.toImmutableList(),
            translateLanguageType = uiState.translateLanguageType,
            onSaveLanguage = {
                handleEvent(SaveLanguage(availableLanguage = it, uiState.translateLanguageType))
            },
            onSelectedOptionsLanguage = {
                handleEvent(SelectedOptionsLanguage(selectedOptionsLanguage = it))
            },
            onDismiss = {
                handleEvent(ToggleLanguageDialog(translateLanguageType = uiState.translateLanguageType))
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        homeDestination = HomeDestination(),
        handleEvent = {},
        uiState = HomeUiState(),
    )
}

private fun handleNotificationPermission(
    textMessage: String,
    context: Context,
) {
    context.startActivity(intentApplicationDetailsPermission(context))
    warning(context, textMessage, Toast.LENGTH_LONG, true).show()
}

private fun Activity?.handleServiceToggle(
    isServiceRunning: Boolean,
    handleEvent: (HomeEvent) -> Unit,
    launchScreenShot: (Activity) -> Unit,
) {
    if (isServiceRunning) {
        this?.stopScreenShotService()
        handleEvent(ToggleServiceButton)
    } else {
        this?.let { launchScreenShot(it) }
    }
}

private fun getInitialEnabledType(): TypeActionScreenshot {
    return if (!isScreenCaptureByDevice) {
        FLOATING_BALLOON
    } else {
        DEVICE_BUTTON
    }
}

private fun Activity.startScreenShotService() =
    screenShotServiceIntent(this).also {
        startService(it)
    }

private fun Activity.startScreenShotServiceWithResult(
    resultData: Intent?,
) = screenShotServiceIntentWithMediaProjection(this, resultData).also {
    startService(it)
}

private fun Activity.stopScreenShotService() =
    screenShotServiceIntent(this).also {
        stopService(it)
    }

private fun Activity.mediaProjectionIntent() =
    (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
        .createScreenCaptureIntent()

private fun intentApplicationDetailsPermission(context: Context) =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}"),
    )

private fun TypeActionScreenshot.isUpsideDownCakeAndNotDeviceButton(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && this != DEVICE_BUTTON
}
