@file:Suppress("LongParameterList")

package com.lingshot.screenshot_presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.TextToSpeechFacade
import com.lingshot.common.helper.launchWithStatus
import com.lingshot.domain.PromptChatGPTConstant.PROMPT_CORRECT_SPELLING
import com.lingshot.domain.PromptChatGPTConstant.PROMPT_TRANSLATE
import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.MessageDomain
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.domain.repository.TextIdentifierRepository
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.lingshot.phrasemaster_presentation.ui.PhraseState
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage.CROPPED_IMAGE
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage.FOCUS_IMAGE
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.FOCUS
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.LANGUAGE
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.LISTEN
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.TRANSLATE
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.model.encodeId
import com.phrase.phrasemaster_domain.usecase.CheckSavedPhraseUseCase
import com.phrase.phrasemaster_domain.usecase.SaveOrDeleteResult
import com.phrase.phrasemaster_domain.usecase.SavePhraseLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ScreenShotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val chatGPTRepository: ChatGPTRepository,
    private val textIdentifierRepository: TextIdentifierRepository,
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val checkSavedPhraseUseCase: CheckSavedPhraseUseCase,
    private val savePhraseLanguageUseCase: SavePhraseLanguageUseCase,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenShotUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech = TextToSpeechFacade(context) { status ->
        _uiState.update { it.copy(screenShotStatus = status) }
    }

    fun handleEvent(screenShotEvent: ScreenShotEvent) {
        when (screenShotEvent) {
            is ScreenShotEvent.CroppedImage -> {
                croppedImage(screenShotEvent.actionCropImage)
            }

            is ScreenShotEvent.FetchCorrectedOriginalText -> {
                fetchCorrectedOriginalText(screenShotEvent.originalText)
            }

            is ScreenShotEvent.FetchTextRecognizer -> {
                fetchTextRecognizer(screenShotEvent.imageBitmap, screenShotEvent.illegiblePhrase)
            }

            is ScreenShotEvent.SaveLanguage -> {
                saveLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SavePhraseInLanguageCollection -> {
                saveOrDeletePhraseInLanguageCollection(screenShotEvent.phraseDomain)
            }

            is ScreenShotEvent.SelectedOptionsLanguage -> {
                selectedOptionsLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SelectedOptionsNavigationBar -> {
                selectedOptionsNavigationBar(screenShotEvent.navigationBarItem)
            }

            is ScreenShotEvent.SetPhraseDomain -> {
                setPhraseDomain(
                    screenShotEvent.originalText,
                    screenShotEvent.translatedText
                )
            }

            is ScreenShotEvent.CheckPhraseInLanguageCollection -> {
                checkPhraseInLanguageCollection(screenShotEvent.originalText)
            }

            is ScreenShotEvent.HideEditPhraseFullScreenDialog -> {
                hideEditPhraseFullScreenDialog()
            }

            is ScreenShotEvent.ClearStatus -> {
                clearStatus()
            }

            is ScreenShotEvent.ToggleLanguageDialog -> {
                toggleLanguageDialog()
            }

            is ScreenShotEvent.ToggleLanguageDialogAndHideSelectionAlert -> {
                toggleLanguageDialogAndHideSelectionAlert()
            }

            is ScreenShotEvent.ToggleDictionaryFullScreenDialog -> {
                toggleDictionaryFullScreenDialog(screenShotEvent.url)
            }
        }
    }

    private fun croppedImage(actionCropImageType: ActionCropImage?) {
        _uiState.update { it.copy(actionCropImage = actionCropImageType) }
    }

    private fun selectedOptionsLanguage(availableLanguage: AvailableLanguage?) {
        _uiState.update { it.copy(availableLanguage = availableLanguage) }
    }

    private fun selectedOptionsNavigationBar(navigationBarItem: NavigationBarItem) {
        when (navigationBarItem) {
            TRANSLATE -> {
                viewModelScope.launch {
                    getLanguage()?.let {
                        croppedImage(CROPPED_IMAGE)
                    } ?: run {
                        showLanguageSelectionAlert()
                    }
                }
            }

            LISTEN -> {
                croppedImage(CROPPED_IMAGE)
            }

            FOCUS -> {
                croppedImage(FOCUS_IMAGE)
            }

            LANGUAGE -> {
                toggleLanguageDialog()
            }
        }
        _uiState.update { it.copy(navigationBarItem = navigationBarItem) }
    }

    private fun showLanguageSelectionAlert() {
        _uiState.update {
            it.copy(
                isLanguageSelectionAlertVisible = !it.isLanguageSelectionAlertVisible
            )
        }
    }

    private fun toggleLanguageDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLanguageDialogVisible = !it.isLanguageDialogVisible,
                    availableLanguage = getLanguage()
                )
            }
        }
    }

    private fun toggleLanguageDialogAndHideSelectionAlert() {
        _uiState.update {
            it.copy(
                isLanguageDialogVisible = !it.isLanguageDialogVisible,
                isLanguageSelectionAlertVisible = !it.isLanguageSelectionAlertVisible
            )
        }
    }

    private fun toggleDictionaryFullScreenDialog(url: String?) {
        _uiState.update { it.copy(dictionaryUrl = url) }
    }

    private fun clearStatus() {
        _uiState.update {
            it.copy(
                screenShotStatus = statusDefault(),
                correctedOriginalTextStatus = statusDefault(),
                isPhraseSaved = false
            )
        }
    }

    private fun hideEditPhraseFullScreenDialog() {
        _uiState.update { it.copy(isEditFullScreenDialogVisible = !it.isEditFullScreenDialogVisible) }
    }

    private fun setPhraseDomain(originalText: String, translatedText: String) {
        val phraseDomain = PhraseDomain(
            id = originalText.encodeId(),
            original = originalText,
            translate = translatedText
        )

        if (_uiState.value.isPhraseSaved) {
            saveOrDeletePhraseInLanguageCollection(phraseDomain)
            return
        }
        _uiState.update {
            it.copy(
                phraseState = PhraseState(phraseDomain, true),
                isEditFullScreenDialogVisible = true
            )
        }
    }

    private fun fetchTextRecognizer(imageBitmap: Bitmap?, illegiblePhrase: String) {
        viewModelScope.launch {
            when (val status = textIdentifierRepository.fetchTextRecognizer(imageBitmap)) {
                is Status.Success -> {
                    val textFormatted = status.data.formatText()
                    when (_uiState.value.navigationBarItem) {
                        TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                        LISTEN -> fetchLanguageIdentifier(textFormatted, illegiblePhrase)
                        else -> Unit
                    }
                }

                is Status.Error -> {
                    _uiState.update { value ->
                        value.copy(
                            screenShotStatus = statusError(status.statusMessage)
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun fetchPhraseToTranslate(text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launchWithStatus({
                val requestBody = ChatGPTPromptBodyDomain(
                    messages = listOf(
                        MessageDomain(
                            role = "system",
                            content = PROMPT_TRANSLATE(getLanguage()?.name?.lowercase())
                        ),
                        MessageDomain(
                            role = "user",
                            content = text
                        )
                    )
                )
                LanguageTranslationDomain(
                    originalText = text,
                    translatedText = chatGPTRepository.get(requestBody),
                    languageCodeFrom = languageIdentifierUseCase(text),
                    languageCodeTo = getLanguage()?.languageCode.toString()
                )
            }, { status ->
                _uiState.update { it.copy(screenShotStatus = status) }
            })
        } else {
            _uiState.update { it.copy(screenShotStatus = statusEmpty()) }
        }
    }

    private fun fetchCorrectedOriginalText(originalText: String) {
        viewModelScope.launchWithStatus({
            val requestBody = ChatGPTPromptBodyDomain(
                messages = listOf(
                    MessageDomain(
                        role = "system",
                        content = PROMPT_CORRECT_SPELLING
                    ),
                    MessageDomain(
                        role = "user",
                        content = originalText
                    )
                )
            )
            chatGPTRepository.get(requestBody)
        }, { status ->
            _uiState.update { it.copy(correctedOriginalTextStatus = status) }
        })
    }

    private fun fetchLanguageIdentifier(text: String, illegiblePhrase: String) {
        viewModelScope.launch {
            val newText = text.ifBlank { illegiblePhrase }
            when (val status = textIdentifierRepository.fetchLanguageIdentifier(newText)) {
                is Status.Success -> {
                    textToSpeech.speakText(newText, status.data.toString())
                }

                is Status.Error -> {
                    _uiState.update { value ->
                        value.copy(
                            screenShotStatus = statusError(status.statusMessage)
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

    private suspend fun getLanguage(): AvailableLanguage? {
        return languageChoiceRepository.getLanguage().first()
    }

    private fun saveLanguage(availableLanguage: AvailableLanguage?) {
        viewModelScope.launch {
            languageChoiceRepository.saveLanguage(availableLanguage)
        }
    }

    private fun saveOrDeletePhraseInLanguageCollection(
        phraseDomain: PhraseDomain
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(phraseState = it.phraseState.copy(isValidLanguage = true))
            }
            when (val result = savePhraseLanguageUseCase(phraseDomain)) {
                is SaveOrDeleteResult.InvalidLanguage -> {
                    _uiState.update {
                        it.copy(phraseState = it.phraseState.copy(isValidLanguage = false))
                    }
                }
                is SaveOrDeleteResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isPhraseSaved = result.isPhraseSaved,
                            isEditFullScreenDialogVisible = false
                        )
                    }
                }
            }
        }
    }

    private fun checkPhraseInLanguageCollection(
        originalText: String
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPhraseSaved = checkSavedPhraseUseCase(originalText)
                )
            }
        }
    }

    private fun String?.formatText(): String {
        return toString()
            .replace("\n", " ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }
}
