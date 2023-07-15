package com.lingshot.screenshot_presentation

import android.content.Context
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.*
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.launchWithStatus
import com.lingshot.domain.PromptChatGPTConstant.PROMPT_CORRECT_SPELLING
import com.lingshot.domain.PromptChatGPTConstant.PROMPT_TRANSLATE
import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.LanguageCodeFromAndToDomain
import com.lingshot.domain.model.PhraseDomain
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusLoading
import com.lingshot.domain.model.statusSuccess
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.domain.repository.PhraseCollectionRepository
import com.lingshot.domain.usecase.SavePhraseLanguageUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import com.lingshot.screenshot_domain.repository.ScreenShotRepository
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage.CROPPED_IMAGE
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage.FOCUS_IMAGE
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.FOCUS
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.LANGUAGE
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.LISTEN
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.TRANSLATE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
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
    private val screenShotRepository: ScreenShotRepository,
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val phraseCollectionRepository: PhraseCollectionRepository,
    private val savePhraseLanguageUseCase: SavePhraseLanguageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenShotUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != SUCCESS) {
                _uiState.update {
                    it.copy(screenShotStatus = statusError(STATUS_TEXT_TO_SPEECH_FAILED))
                }
            }
        }
    }

    init {
        setupTextToSpeech()
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
                fetchTextRecognizer(screenShotEvent.imageBitmap)
            }

            is ScreenShotEvent.SaveLanguage -> {
                saveLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SavePhraseInLanguageCollection -> {
                savePhraseInLanguageCollection(
                    screenShotEvent.originalText,
                    screenShotEvent.translatedText
                )
            }

            is ScreenShotEvent.SelectedOptionsLanguage -> {
                selectedOptionsLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SelectedOptionsNavigationBar -> {
                selectedOptionsNavigationBar(screenShotEvent.navigationBarItem)
            }

            is ScreenShotEvent.CheckPhraseInLanguageCollection -> {
                checkPhraseInLanguageCollection(
                    screenShotEvent.originalText
                )
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

            is ScreenShotEvent.ToggleDictionaryFullScreenPopup -> {
                toggleDictionaryFullScreenPopup(screenShotEvent.url)
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

    private fun toggleDictionaryFullScreenPopup(url: String?) {
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

    private fun fetchTextRecognizer(imageBitmap: Bitmap?) {
        viewModelScope.launch {
            when (val status = screenShotRepository.fetchTextRecognizer(imageBitmap)) {
                is Status.Success -> {
                    val textFormatted = status.data.formatText()
                    when (_uiState.value.navigationBarItem) {
                        TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                        LISTEN -> fetchLanguageIdentifier(textFormatted)
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
                    prompt = PROMPT_TRANSLATE(getLanguage()?.displayName, text)
                )
                LanguageTranslationDomain(
                    originalText = text,
                    translatedText = chatGPTRepository.get(requestBody),
                    languageCodeFrom = text.getLanguageCodeIdentifier(),
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
                prompt = PROMPT_CORRECT_SPELLING(originalText)
            )
            chatGPTRepository.get(requestBody)
        }, { status ->
            _uiState.update { it.copy(correctedOriginalTextStatus = status) }
        })
    }

    private fun fetchLanguageIdentifier(text: String) {
        viewModelScope.launch {
            when (val status = screenShotRepository.fetchLanguageIdentifier(text)) {
                is Status.Success -> {
                    fetchTextToSpeech(text.ifBlank { ILLEGIBLE_TEXT }, status.data.toString())
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

    private fun fetchTextToSpeech(text: String, languageCode: String) =
        with(textToSpeech) {
            val languageLocale = if (languageCode == LANGUAGE_CODE_UNAVAILABLE) {
                Locale.US
            } else {
                Locale.forLanguageTag(languageCode)
            }

            val result = setLanguage(languageLocale)
            if (result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED) {
                _uiState.update { value ->
                    value.copy(
                        screenShotStatus = statusError(
                            STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED
                        )
                    )
                }
            }

            speak(text, QUEUE_FLUSH, null, "")
        }

    private fun setupTextToSpeech() =
        with(textToSpeech) {
            setOnUtteranceProgressListener(onSpeechListener())
            setSpeechRate(0.7f)
        }

    private fun onSpeechListener() = object : UtteranceProgressListener() {
        override fun onStart(p0: String?) {
            _uiState.update { it.copy(screenShotStatus = statusLoading()) }
        }

        override fun onDone(value: String?) {
            _uiState.update { it.copy(screenShotStatus = statusSuccess(null)) }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(p0: String?) {
            _uiState.update {
                it.copy(screenShotStatus = statusError(p0))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
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

    private fun savePhraseInLanguageCollection(
        originalText: String,
        translatedText: String
    ) {
        viewModelScope.launch {
            val languageDomain = originalText.getLanguageCodeFromAndToDomain()
            val phraseDomain = PhraseDomain(original = originalText, translate = translatedText)
            _uiState.update {
                it.copy(
                    isPhraseSaved = savePhraseLanguageUseCase(
                        languageDomain,
                        phraseDomain
                    )
                )
            }
        }
    }

    private fun checkPhraseInLanguageCollection(
        originalText: String
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPhraseSaved = phraseCollectionRepository.isPhraseSaved(
                        originalText.getLanguageCodeFromAndToDomain().name,
                        originalText
                    )
                )
            }
        }
    }

    private suspend fun String.getLanguageCodeFromAndToDomain(): LanguageCodeFromAndToDomain {
        return LanguageCodeFromAndToDomain(
            name = getLanguageCodeIdentifier() + "_" + getLanguage()?.languageCode.toString()
        )
    }

    private suspend fun String.getLanguageCodeIdentifier(): String {
        val status = screenShotRepository.fetchLanguageIdentifier(this)
        if (status is Status.Success) {
            return status.data.orEmpty()
        }
        return ""
    }

    private fun String?.formatText(): String {
        return toString()
            .replace("\n", " ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }

    companion object {
        const val ILLEGIBLE_TEXT = "There isn't any legible text."
        private const val LANGUAGE_CODE_UNAVAILABLE = "und"
        private const val STATUS_TEXT_TO_SPEECH_FAILED = "Text to speech failed."
        private const val STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED = "Text to speech not supported."
    }
}
