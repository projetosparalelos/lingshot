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

package com.lingshot.subtitle_presentation.ui

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.domain.model.Status
import com.lingshot.subtitle_presentation.R
import com.lingshot.subtitle_presentation.SubtitleEvent
import com.lingshot.subtitle_presentation.SubtitleEvent.CroppedImage
import com.lingshot.subtitle_presentation.SubtitleEvent.FetchTextRecognizerSelect
import com.lingshot.subtitle_presentation.SubtitleEvent.FetchTextTranslate
import com.lingshot.subtitle_presentation.SubtitleEvent.FocusImage
import com.lingshot.subtitle_presentation.SubtitleUiState
import com.lingshot.subtitle_presentation.SubtitleViewModel
import com.lingshot.subtitle_presentation.ui.component.SubtitleBottomSheet
import com.lingshot.subtitle_presentation.ui.component.SubtitleImage
import com.lingshot.subtitle_presentation.ui.component.SubtitleLottieLoading
import es.dmoral.toasty.Toasty.info
import es.dmoral.toasty.Toasty.warning

@Composable
internal fun SubtitleRoute(
    viewModel: SubtitleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubtitleScreen(
        uiState = uiState,
        isLanguageAvailable = viewModel::isLanguageAvailable,
        handleEvent = viewModel::handleEvent,
    )
}

@Composable
internal fun SubtitleScreen(
    uiState: SubtitleUiState,
    isLanguageAvailable: (Bitmap?, (Boolean) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    handleEvent: (event: SubtitleEvent) -> Unit,
) {
    val listSubtitleImage = remember { mutableStateListOf<Bitmap?>() }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color.Black),
    ) {
        val messageIllegiblePhrase = stringResource(id = com.lingshot.common.R.string.text_message_illegible_phrase)
        SubtitleImage(
            isImageWithZoom = (uiState.subtitleStatus is Status.Success),
            actionCropImage = uiState.actionCropImage,
            onCropImageResult = { bitmap ->
                isLanguageAvailable(bitmap) { isAvailable ->
                    if (isAvailable) {
                        listSubtitleImage.add(bitmap)
                    } else {
                        warning(context, messageIllegiblePhrase).show()
                    }
                }
            },
            onCroppedImage = {
                handleEvent(CroppedImage(it))
            },
        )

        if (uiState.subtitleStatus.isLoadingStatus) {
            SubtitleLottieLoading(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
            )
        }

        val messageInfoAddPhrase = stringResource(id = R.string.text_message_info_add_phrase)
        SubtitleBottomSheet(
            status = uiState.subtitleStatus,
            countPhraseSelected = listSubtitleImage.size,
            onFetchTextRecognizerSelect = {
                handleEvent(FetchTextRecognizerSelect)
            },
            onFetchTextTranslate = {
                if (listSubtitleImage.size >= 1) {
                    handleEvent(FetchTextTranslate(listSubtitleImage))
                } else {
                    info(context, messageInfoAddPhrase).show()
                }
            },
            onFocusImage = {
                handleEvent(FocusImage)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubtitleScreenPreview() {
    SubtitleScreen(
        uiState = SubtitleUiState(),
        isLanguageAvailable = { _, _ -> },
        handleEvent = {},
    )
}
