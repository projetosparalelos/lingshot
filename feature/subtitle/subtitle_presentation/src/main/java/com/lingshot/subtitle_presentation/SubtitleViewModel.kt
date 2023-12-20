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
@file:Suppress("LongParameterList")

package com.lingshot.subtitle_presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.analytics.constant.LANGUAGE_UND_VALUE
import com.lingshot.analytics.constant.ORIGINAL_CONTENT
import com.lingshot.analytics.constant.TRANSLATE_CONTENT
import com.lingshot.analytics.constant.TYPE_SCREEN_CAPTURE_ITEM
import com.lingshot.analytics.constant.TYPE_SCREEN_CAPTURE_SUBTITLE_VALUE
import com.lingshot.analytics.helper.AnalyticsEventHelper
import com.lingshot.common.helper.launchWithStatus
import com.lingshot.common.util.formatText
import com.lingshot.designsystem.component.ActionCropImage
import com.lingshot.designsystem.component.ActionCropImage.CROPPED_IMAGE
import com.lingshot.designsystem.component.ActionCropImage.FOCUS_IMAGE
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.domain.model.statusError
import com.lingshot.domain.repository.TextIdentifierRepository
import com.lingshot.domain.usecase.TranslateApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubtitleViewModel @Inject constructor(
    private val analyticsEventHelper: AnalyticsEventHelper,
    private val textIdentifierRepository: TextIdentifierRepository,
    private val translateApiUseCase: TranslateApiUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubtitleUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(screenShotEvent: SubtitleEvent) {
        when (screenShotEvent) {
            is SubtitleEvent.ClearStatus -> {
                clearStatus()
            }

            is SubtitleEvent.CroppedImage -> {
                croppedImage(screenShotEvent.actionCropImage)
            }

            is SubtitleEvent.FetchTextRecognizerSelect -> {
                croppedImage(CROPPED_IMAGE)
            }

            is SubtitleEvent.FetchTextTranslate -> {
                fetchTextToTranslate(screenShotEvent.listSubtitle)
            }

            is SubtitleEvent.FocusImage -> {
                croppedImage(FOCUS_IMAGE)
            }
        }
    }

    private fun croppedImage(actionCropImageType: ActionCropImage?) {
        _uiState.update { it.copy(actionCropImage = actionCropImageType) }
    }

    private fun clearStatus() {
        _uiState.update {
            it.copy(
                subtitleStatus = statusDefault(),
            )
        }
    }

    private fun fetchTextToTranslate(listSubtitleImage: List<Bitmap?>) {
        viewModelScope.launchWithStatus({
            withContext(Dispatchers.IO) {
                val deferredList = listSubtitleImage.map { bitmap ->
                    async {
                        runCatching {
                            when (
                                val status =
                                    textIdentifierRepository.fetchTextRecognizer(bitmap)
                            ) {
                                is Status.Success -> {
                                    val textFormatted = status.data.formatText()
                                    Subtitle(
                                        translateApiUseCase(textFormatted).toString(),
                                        bitmap,
                                    ).also {
                                        analyticsEventHelper.sendSelectItem(
                                            TRANSLATE_CONTENT to it.text,
                                            ORIGINAL_CONTENT to textFormatted,
                                            TYPE_SCREEN_CAPTURE_ITEM to TYPE_SCREEN_CAPTURE_SUBTITLE_VALUE,
                                        )
                                    }
                                }

                                else -> null
                            }
                        }.getOrElse { t ->
                            _uiState.update { it.copy(subtitleStatus = statusError(t.message)) }
                            null
                        }
                    }
                }
                deferredList.awaitAll().filterNotNull()
            }
        }, { status ->
            _uiState.update { it.copy(subtitleStatus = status) }
        })
    }

    fun isLanguageAvailable(bitmap: Bitmap?, block: (Boolean) -> Unit) {
        viewModelScope.launch {
            val status = textIdentifierRepository.fetchTextRecognizer(bitmap)
            if (status is Status.Success) {
                val text = status.data.toString()

                if (text.isEmpty()) {
                    analyticsEventHelper.sendSelectItem(
                        TRANSLATE_CONTENT to LANGUAGE_UND_VALUE,
                        ORIGINAL_CONTENT to text,
                        TYPE_SCREEN_CAPTURE_ITEM to TYPE_SCREEN_CAPTURE_SUBTITLE_VALUE,
                    )
                }
                block(text.isNotEmpty())
            }
        }
    }
}

data class Subtitle(val text: String, val bitmap: Bitmap?)
