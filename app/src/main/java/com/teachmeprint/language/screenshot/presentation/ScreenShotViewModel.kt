package com.teachmeprint.language.screenshot.presentation

import android.content.Context
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.*
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teachmeprint.language.core.common.helper.*
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_IDENTIFY_LANGUAGE_FAILED
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_TEXT_ERROR_GENERIC
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_TEXT_RECOGNIZER_FAILED
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_ERROR
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_FAILED
import com.teachmeprint.language.core.common.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.screenshot.presentation.ScreenShotStatus.*
import com.teachmeprint.language.screenshot.presentation.ui.component.ActionCropImage
import com.teachmeprint.language.screenshot.presentation.ui.component.ActionCropImage.CROPPED_IMAGE
import com.teachmeprint.language.screenshot.presentation.ui.component.ActionCropImage.FOCUS_IMAGE
import com.teachmeprint.language.screenshot.presentation.ui.component.NavigationBarItem
import com.teachmeprint.language.screenshot.presentation.ui.component.NavigationBarItem.FOCUS
import com.teachmeprint.language.screenshot.presentation.ui.component.NavigationBarItem.LANGUAGE
import com.teachmeprint.language.screenshot.presentation.ui.component.NavigationBarItem.LISTEN
import com.teachmeprint.language.screenshot.presentation.ui.component.NavigationBarItem.TRANSLATE
import com.teachmeprint.language.screenshot.data.repository.ScreenShotRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ScreenShotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val screenShotRepository: ScreenShotRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenShotUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != SUCCESS) {
                _uiState.update {
                    it.copy(screenShotStatus = Error(STATUS_TEXT_TO_SPEECH_FAILED))
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

            is ScreenShotEvent.FetchTextRecognizer -> {
                fetchTextRecognizer(screenShotEvent.imageBitmap)
            }

            is ScreenShotEvent.SaveLanguage -> {
                saveLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SelectedOptionsLanguage -> {
                selectedOptionsLanguage(screenShotEvent.availableLanguage)
            }

            is ScreenShotEvent.SelectedOptionsNavigationBar -> {
                selectedOptionsNavigationBar(screenShotEvent.navigationBarItem)
            }

            is ScreenShotEvent.HideTranslateBalloon -> {
                hideTranslateBalloon()
            }

            is ScreenShotEvent.ShowTranslateBalloon -> {
                showTranslateBalloon(screenShotEvent.textTranslate)
            }

            is ScreenShotEvent.ToggleLanguageDialog -> {
                toggleLanguageDialog()
            }

            is ScreenShotEvent.ToggleLanguageDialogAndHideSelectionAlert -> {
                toggleLanguageDialogAndHideSelectionAlert()
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
                getLanguage()?.let {
                    croppedImage(CROPPED_IMAGE)
                } ?: run {
                   showLanguageSelectionAlert()
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
        _uiState.update { it.copy(isLanguageSelectionAlertVisible = !it.isLanguageSelectionAlertVisible) }
    }

    private fun toggleLanguageDialog() {
        _uiState.update {
            it.copy(
                isLanguageDialogVisible = !it.isLanguageDialogVisible,
                availableLanguage = getLanguage()
            )
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

    private fun hideTranslateBalloon() {
        _uiState.update {
            it.copy(
                isBalloonTranslateVisible = !it.isBalloonTranslateVisible
            )
        }
    }

    private fun showTranslateBalloon(textTranslate: String) {
        _uiState.update {
            it.copy(
                isBalloonTranslateVisible = !it.isBalloonTranslateVisible,
                textTranslate = textTranslate
            )
        }
    }

    private fun fetchTextRecognizer(imageBitmap: Bitmap?) {
        screenShotRepository.fetchTextRecognizer(imageBitmap)
            ?.addOnSuccessListener { value ->
                val textFormatted = value.text.formatText()
                when (_uiState.value.navigationBarItem) {
                    TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                    LISTEN -> fetchLanguageIdentifier(textFormatted)
                    else -> {}
                }
            }
            ?.addOnFailureListener {
                _uiState.update { value ->
                    value.copy(screenShotStatus = Error(STATUS_TEXT_RECOGNIZER_FAILED))
                }
            }
    }

    private fun fetchPhraseToTranslate(text: String) {
        _uiState.update { it.copy(screenShotStatus = Loading) }
        viewModelScope.launch {
                runCatching {
                    screenShotRepository.saveTranslationCount()
                    withContext(Dispatchers.IO) {
                        if (text.isBlank()) {
                            delay(500.milliseconds)
                            return@withContext ILLEGIBLE_TEXT
                        }
                       screenShotRepository.getTranslatePhrase(text)
                    }
                }.onSuccess { text ->
                    _uiState.update { it.copy(screenShotStatus = Success(text)) }
                }.onFailure { e ->
                    when(e) {
                        is HttpException -> {
                            _uiState.update { it.copy(screenShotStatus = Error(e.code())) }
                        }
                        is IOException -> {
                            _uiState.update { it.copy(screenShotStatus = Error(STATUS_TEXT_ERROR_GENERIC)) }
                        }
                    }
                }
            }
    }

    private fun fetchLanguageIdentifier(text: String) {
        screenShotRepository.fetchLanguageIdentifier(text)
            .addOnSuccessListener { languageCode ->
                fetchTextToSpeech(text.ifBlank { ILLEGIBLE_TEXT }, languageCode)
            }
            .addOnFailureListener {
                _uiState.update { value ->
                    value.copy(screenShotStatus = Error(STATUS_IDENTIFY_LANGUAGE_FAILED))
                }
            }
    }

    private fun fetchTextToSpeech(text: String, languageCode: String) =
        with(textToSpeech) {
            val languageLocale = if (languageCode == LANGUAGE_CODE_UNAVAILABLE) Locale.US
            else Locale.forLanguageTag(languageCode)

            val result = setLanguage(languageLocale)
            if (result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED) {
                _uiState.update { value ->
                    value.copy(screenShotStatus = Error(STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED))
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
            _uiState.update { it.copy(screenShotStatus = Loading) }
        }

        override fun onDone(value: String?) {
            _uiState.update { it.copy(screenShotStatus = Success(value)) }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(p0: String?) {
            _uiState.update {
                it.copy(screenShotStatus = Error(STATUS_TEXT_TO_SPEECH_ERROR))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun getLanguage(): AvailableLanguage? {
        return screenShotRepository.getLanguage()
    }

    private fun saveLanguage(availableLanguage: AvailableLanguage?) {
        viewModelScope.launch {
            screenShotRepository.saveLanguage(availableLanguage)
        }
    }

    fun hasReachedMaxTranslationCount(): Boolean {
        return screenShotRepository.hasReachedMaxTranslationCount()
    }

    private fun String.formatText(): String {
        return replace("\n", " ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }

    companion object {
        private const val ILLEGIBLE_TEXT = "There isn't any legible text."
        private const val LANGUAGE_CODE_UNAVAILABLE = "und"
    }
}