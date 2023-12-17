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

package com.lingshot.screenshot_presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.analytics.constant.LANGUAGE_UND_VALUE
import com.lingshot.analytics.constant.ORIGINAL_CONTENT
import com.lingshot.analytics.constant.ORIGINAL_LISTENING_VALUE
import com.lingshot.analytics.constant.TRANSLATE_CONTENT
import com.lingshot.analytics.constant.TYPE_SCREEN_CAPTURE_ITEM
import com.lingshot.analytics.constant.TYPE_SCREEN_CAPTURE_STANDARD_VALUE
import com.lingshot.analytics.helper.AnalyticsEventHelper
import com.lingshot.common.helper.TextToSpeechFacade
import com.lingshot.common.helper.launchWithStatus
import com.lingshot.common.util.formatText
import com.lingshot.designsystem.component.ActionCropImage
import com.lingshot.domain.PromptChatGPTConstant.PROMPT_CORRECT_SPELLING
import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.MessageDomain
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.domain.repository.TextIdentifierRepository
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.domain.usecase.TranslateApiUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.FROM
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.TO
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenShotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsEventHelper: AnalyticsEventHelper,
    private val chatGPTRepository: ChatGPTRepository,
    private val textIdentifierRepository: TextIdentifierRepository,
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
    private val translateApiUseCase: TranslateApiUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenShotUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech = TextToSpeechFacade(context) { status ->
        _uiState.update { it.copy(screenShotStatus = status) }
    }

    private var translateJob: Job? = null
    private var correctedOriginalTextJob: Job? = null

    fun handleEvent(screenShotEvent: ScreenShotEvent) {
        when (screenShotEvent) {
            is ScreenShotEvent.CroppedImage -> {
                croppedImage(screenShotEvent.actionCropImage)
            }

            is ScreenShotEvent.FetchCorrectedOriginalText -> {
                fetchCorrectedOriginalText(screenShotEvent.originalText)
            }

            is ScreenShotEvent.FetchLanguageIdentifier -> {
                fetchLanguageIdentifier(screenShotEvent.originalText, screenShotEvent.illegiblePhrase)
            }

            is ScreenShotEvent.FetchTextRecognizer -> {
                fetchTextRecognizer(screenShotEvent.imageBitmap)
            }

            is ScreenShotEvent.ClearStatus -> {
                clearStatus()
            }

            is ScreenShotEvent.ToggleDictionaryFullScreenDialog -> {
                toggleDictionaryFullScreenDialog(screenShotEvent.url)
            }
        }
    }

    private fun croppedImage(actionCropImage: ActionCropImage?) {
        _uiState.update { it.copy(actionCropImage = actionCropImage) }
    }

    private fun toggleDictionaryFullScreenDialog(url: String?) {
        _uiState.update { it.copy(dictionaryUrl = url) }
    }

    private fun clearStatus() {
        _uiState.update {
            it.copy(
                screenShotStatus = statusDefault(),
                correctedOriginalTextStatus = statusDefault(),
            )
        }
    }

    private fun fetchTextRecognizer(imageBitmap: Bitmap?) {
        viewModelScope.launch {
            when (val status = textIdentifierRepository.fetchTextRecognizer(imageBitmap)) {
                is Status.Success -> {
                    updateServiceRunning()

                    val textFormatted = status.data.formatText()
                    fetchPhraseToTranslate(textFormatted)
                }

                is Status.Error -> {
                    _uiState.update { value ->
                        value.copy(
                            screenShotStatus = statusError(status.statusMessage),
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun fetchPhraseToTranslate(text: String) {
        translateJob?.cancel()
        viewModelScope.launch {
            if (text.isNotEmpty()) {
                translateJob = viewModelScope.launchWithStatus({
                    LanguageTranslationDomain(
                        originalText = text,
                        translatedText = translateApiUseCase(text),
                        languageCodeFrom = getLanguageFrom()?.languageCode.toString(),
                        languageCodeTo = getLanguageTo()?.languageCode.toString(),
                        enabledDictionary = getLanguageFrom()?.enabledDictionary == false,
                    ).also {
                        analyticsEventHelper.sendSelectItem(
                            TRANSLATE_CONTENT to it.translatedText.toString(),
                            ORIGINAL_CONTENT to it.originalText,
                            TYPE_SCREEN_CAPTURE_ITEM to TYPE_SCREEN_CAPTURE_STANDARD_VALUE,
                        )
                    }
                }, { status ->
                    _uiState.update { it.copy(screenShotStatus = status) }
                })
            } else {
                analyticsEventHelper.sendSelectItem(
                    TRANSLATE_CONTENT to LANGUAGE_UND_VALUE,
                    ORIGINAL_CONTENT to text,
                    TYPE_SCREEN_CAPTURE_ITEM to TYPE_SCREEN_CAPTURE_STANDARD_VALUE,
                )
                _uiState.update { it.copy(screenShotStatus = statusEmpty()) }
            }
        }.invokeOnCompletion {
            updateServiceRunning()
        }
    }

    private fun fetchCorrectedOriginalText(originalText: String) {
        correctedOriginalTextJob?.cancel()
        correctedOriginalTextJob = viewModelScope.launchWithStatus({
            val requestBody = ChatGPTPromptBodyDomain(
                messages = listOf(
                    MessageDomain(
                        role = "system",
                        content = PROMPT_CORRECT_SPELLING,
                    ),
                    MessageDomain(
                        role = "user",
                        content = originalText,
                    ),
                ),
            )
            chatGPTRepository.get(requestBody)
        }, { status ->
            _uiState.update { it.copy(correctedOriginalTextStatus = status) }
        })
    }

    private fun fetchLanguageIdentifier(text: String, illegiblePhrase: String) {
        viewModelScope.launch {
            val newText = if (text.isLanguageAvailable()) {
                text
            } else {
                illegiblePhrase
            }
            when (val status = textIdentifierRepository.fetchLanguageIdentifier(newText)) {
                is Status.Success -> {
                    analyticsEventHelper.sendSelectItem(
                        ORIGINAL_CONTENT to "$newText-[${ORIGINAL_LISTENING_VALUE}]",
                    )
                    textToSpeech.speakText(newText, status.data.toString())
                }

                is Status.Error -> {
                    _uiState.update { value ->
                        value.copy(
                            screenShotStatus = statusError(status.statusMessage),
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun updateServiceRunning() {
        _uiState.update { it.copy(isRunnable = !it.isRunnable) }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.shutdown()
    }

    private suspend fun getLanguageTo(): AvailableLanguage? {
        return languageChoiceRepository.getLanguage(TO).first()
    }

    private suspend fun getLanguageFrom(): AvailableLanguage? {
        return languageChoiceRepository.getLanguage(FROM).first()
    }

    private suspend fun String.isLanguageAvailable(): Boolean {
        return (
            languageIdentifierUseCase(this) == getLanguageFrom()?.languageCode ||
                getLanguageFrom()?.enabledLanguage == false
            )
    }
}
