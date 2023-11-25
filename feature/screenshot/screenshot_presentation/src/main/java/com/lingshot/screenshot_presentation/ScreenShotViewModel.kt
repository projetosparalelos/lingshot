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
import com.lingshot.common.helper.TextToSpeechFacade
import com.lingshot.common.helper.launchWithStatus
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
import com.lingshot.screenshot_presentation.ui.component.ButtonMenuItem
import com.lingshot.screenshot_presentation.ui.component.ButtonMenuItem.LISTEN
import com.lingshot.screenshot_presentation.ui.component.ButtonMenuItem.TRANSLATE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenShotViewModel @Inject constructor(
    @ApplicationContext context: Context,
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

    fun handleEvent(screenShotEvent: ScreenShotEvent) {
        when (screenShotEvent) {
            is ScreenShotEvent.FetchCorrectedOriginalText -> {
                fetchCorrectedOriginalText(screenShotEvent.originalText)
            }

            is ScreenShotEvent.FetchTextRecognizer -> {
                croppedImage()
                fetchTextRecognizer(screenShotEvent.imageBitmap, screenShotEvent.illegiblePhrase)
            }

            is ScreenShotEvent.SelectedOptionsButtonMenuItem -> {
                selectedOptionsButtonMenuItem(screenShotEvent.buttonMenuItem)
            }

            is ScreenShotEvent.ClearStatus -> {
                clearStatus()
            }

            is ScreenShotEvent.ToggleDictionaryFullScreenDialog -> {
                toggleDictionaryFullScreenDialog(screenShotEvent.url)
            }
        }
    }

    private fun croppedImage() {
        _uiState.update { it.copy(isCrop = !it.isCrop) }
    }

    private fun selectedOptionsButtonMenuItem(buttonMenuItem: ButtonMenuItem) {
        when (buttonMenuItem) {
            TRANSLATE, LISTEN -> {
                viewModelScope.launch {
                    croppedImage()
                }
            }
        }
        _uiState.update { it.copy(buttonMenuItem = buttonMenuItem) }
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

    private fun fetchTextRecognizer(imageBitmap: Bitmap?, illegiblePhrase: String) {
        viewModelScope.launch {
            when (val status = textIdentifierRepository.fetchTextRecognizer(imageBitmap)) {
                is Status.Success -> {
                    val textFormatted = status.data.formatText()
                    when (_uiState.value.buttonMenuItem) {
                        TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                        LISTEN -> fetchLanguageIdentifier(textFormatted, illegiblePhrase)
                    }
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
        viewModelScope.launch {
            if (text.isLanguageAvailable()) {
                viewModelScope.launchWithStatus({
                    LanguageTranslationDomain(
                        originalText = text,
                        translatedText = translateApiUseCase(text),
                        languageCodeFrom = getLanguageFrom()?.languageCode.toString(),
                        languageCodeTo = getLanguageTo()?.languageCode.toString(),
                        enabledDictionary = getLanguageFrom()?.enabledDictionary == false,
                    )
                }, { status ->
                    _uiState.update { it.copy(screenShotStatus = status) }
                })
            } else {
                _uiState.update { it.copy(screenShotStatus = statusEmpty()) }
            }
        }
    }

    private fun fetchCorrectedOriginalText(originalText: String) {
        viewModelScope.launchWithStatus({
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

    private fun String?.formatText(): String {
        return toString()
            .replace("\n", " ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }
}
