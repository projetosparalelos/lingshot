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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.screenshot_presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.common.helper.onEmpty
import com.lingshot.common.helper.onError
import com.lingshot.common.helper.onLoading
import com.lingshot.common.helper.onSuccess
import com.lingshot.common.util.findActivity
import com.lingshot.designsystem.component.LingshotCropImage
import com.lingshot.designsystem.theme.md_theme_dark_tertiary
import com.lingshot.screenshot_domain.model.ReadModeType.SPEECH_BUBBLE
import com.lingshot.screenshot_domain.model.ReadModeType.STANDARD
import com.lingshot.screenshot_presentation.R
import com.lingshot.screenshot_presentation.ScreenShotEvent
import com.lingshot.screenshot_presentation.ScreenShotEvent.ChangeReadMode
import com.lingshot.screenshot_presentation.ScreenShotEvent.ClearStatus
import com.lingshot.screenshot_presentation.ScreenShotEvent.CroppedImage
import com.lingshot.screenshot_presentation.ScreenShotEvent.FetchTextRecognizer
import com.lingshot.screenshot_presentation.ScreenShotEvent.ToggleDictionaryFullScreenDialog
import com.lingshot.screenshot_presentation.ScreenShotUiState
import com.lingshot.screenshot_presentation.ScreenShotViewModel
import com.lingshot.screenshot_presentation.ui.component.ScreenShotDictionaryFullScreenDialog
import com.lingshot.screenshot_presentation.ui.component.ScreenShotDrawSpeech
import com.lingshot.screenshot_presentation.ui.component.ScreenShotLottieLoading
import com.lingshot.screenshot_presentation.ui.component.ScreenShotReadModeMenu
import com.lingshot.screenshot_presentation.ui.component.ScreenShotSnackBarError
import com.lingshot.screenshot_presentation.ui.component.ScreenShotTranslateBottomSheet
import es.dmoral.toasty.Toasty.warning

@Composable
internal fun ScreenShotRoute(
    viewModel: ScreenShotViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenShotScreen(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )
}

@Composable
internal fun ScreenShotScreen(
    uiState: ScreenShotUiState,
    modifier: Modifier = Modifier,
    handleEvent: (event: ScreenShotEvent) -> Unit,
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color.Black),
    ) {
        val illegiblePhrase =
            stringResource(id = com.lingshot.common.R.string.text_message_illegible_phrase)
        LingshotCropImage(
            isAutomaticCropperEnabled = true,
            isRunnable = uiState.isRunnable,
            actionCropImage = uiState.actionCropImage,
            onClear = {
                handleEvent(ClearStatus)
            },
            onCropImageResult = { bitmap ->
                handleEvent(FetchTextRecognizer(bitmap))
            },
            onCroppedImage = { actionCropImage ->
                handleEvent(CroppedImage(actionCropImage))
            },
            content = { rect ->
                if (uiState.readModeType == SPEECH_BUBBLE) {
                    uiState.screenShotStatus.onSuccess {
                        ScreenShotDrawSpeech(
                            boundingBox = rect,
                            translatedText = it.translatedText.toString(),
                        )
                    }
                }
            },
        )
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = md_theme_dark_tertiary,
                actionIconContentColor = md_theme_dark_tertiary,
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        activity?.finish()
                    },
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                ScreenShotReadModeMenu(
                    readModeType = uiState.readModeType,
                    onChangeReadMode = { readModeType ->
                        handleEvent(ChangeReadMode(readModeType))
                    },
                    onClear = {
                        handleEvent(ClearStatus)
                    },
                )
            },
            title = {},
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            uiState.screenShotStatus.onEmpty {
                warning(context, illegiblePhrase).show()
                handleEvent(ClearStatus)
            }.onLoading {
                ScreenShotLottieLoading(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    loading = R.raw.loading_translate,
                )
            }.onSuccess {
                if (uiState.readModeType == STANDARD) {
                    ScreenShotTranslateBottomSheet(
                        languageTranslationDomain = it,
                        correctedOriginalTextStatus = uiState.correctedOriginalTextStatus,
                        onCorrectedOriginalText = { original ->
                            handleEvent(ScreenShotEvent.FetchCorrectedOriginalText(original))
                        },
                        onToggleDictionaryFullScreenDialog = { url ->
                            handleEvent(ToggleDictionaryFullScreenDialog(url))
                        },
                        onDismiss = {
                            handleEvent(ClearStatus)
                        },
                    )
                }
            }.onError {
                ScreenShotSnackBarError(
                    modifier = Modifier.padding(bottom = 16.dp),
                    message = it,
                    onDismiss = {
                        handleEvent(ClearStatus)
                    },
                )
            }
        }
    }
    uiState.dictionaryUrl?.let { url ->
        ScreenShotDictionaryFullScreenDialog(url) {
            handleEvent(ToggleDictionaryFullScreenDialog(null))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotScreenPreview() {
    ScreenShotScreen(
        uiState = ScreenShotUiState(),
        handleEvent = {},
    )
}
