package com.teachmeprint.language.feature.screenshot.presentation

import android.content.Context
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.*
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage.fromBitmap
import com.google.mlkit.vision.text.TextRecognizer
import com.teachmeprint.language.core.helper.*
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_IDENTIFY_LANGUAGE_FAILED
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_TEXT_ERROR_GENERIC
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_TEXT_RECOGNIZER_FAILED
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_ERROR
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_FAILED
import com.teachmeprint.language.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum.LISTEN
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum.TRANSLATE
import com.teachmeprint.language.data.model.screenshot.entity.RequestBody
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType
import com.teachmeprint.language.feature.screenshot.model.event.ScreenShotEvent
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotStatus.*
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotUiState
import com.teachmeprint.language.feature.screenshot.repository.ScreenShotRepository
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScreenShotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val screenShotRepository: ScreenShotRepository,
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
) : ViewModel() {

    private val _response = MutableLiveResource<String>()
    val response by getLiveData(_response)

    private val _uiState = MutableStateFlow(ScreenShotUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != SUCCESS) {
                _response.error(STATUS_TEXT_TO_SPEECH_FAILED)
            }
        }
    }

    val typeIndicatorEnum: TypeIndicatorEnum = TRANSLATE

    init {
        setupTextToSpeech()
    }

    fun handleEvent(screenShotEvent: ScreenShotEvent) {
        when (screenShotEvent) {
            is ScreenShotEvent.FetchTextRecognizer -> {
                fetchTextRecognizer(screenShotEvent.imageBitmap)
            }
            is ScreenShotEvent.ShowBalloon -> {
                showBalloon(screenShotEvent.textTranslate)
            }
            is ScreenShotEvent.CroppedImage -> {
                croppedImage(screenShotEvent.actionCropImageType)
            }
            is ScreenShotEvent.OptionSelectedLanguage -> {
                selectedOptionLanguage(screenShotEvent.selectedOptionLanguage)
            }
            is ScreenShotEvent.OptionSelectedNavigationBar -> {
                selectedOptionNavigationBar(screenShotEvent.selectedOptionNavigationBar)
            }
            is ScreenShotEvent.SaveLanguage -> {
                saveLanguage(screenShotEvent.availableLanguage)
            }
            is ScreenShotEvent.ShowDialogLanguage -> {
                showDialogLanguage()
            }
        }
    }

    private fun showDialogLanguage() {
        _uiState.update { it.copy(showDialogLanguage = !it.showDialogLanguage, selectedOptionLanguage = getLanguage()) }
    }

    private fun selectedOptionNavigationBar(navigationBarItemType: NavigationBarItemType) {
        _uiState.update { it.copy(selectedOptionNavigationBar = navigationBarItemType) }
    }

    private fun selectedOptionLanguage(availableLanguage: AvailableLanguage?) {
        _uiState.update { it.copy(selectedOptionLanguage = availableLanguage) }
    }

    private fun croppedImage(actionCropImageType: ActionCropImageType?) {
        _uiState.update { it.copy(actionCropImageType = actionCropImageType) }
    }

    private fun showBalloon(textTranslate: String) {
        _uiState.update { it.copy(showBalloon = !it.showBalloon, textTranslate = textTranslate) }
    }

    private fun fetchTextRecognizer(imageBitmap: Bitmap?) {
        val inputImage = imageBitmap?.let { fromBitmap(it, 0) }
        inputImage?.let {
            textRecognizer.process(it)
                .addOnSuccessListener { value ->
                    val textFormatted = value.text.checkTextAndFormat()
                    when (typeIndicatorEnum) {
                        TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                        LISTEN -> fetchLanguageIdentifier(textFormatted)
                    }
                }
                .addOnFailureListener {
                    _uiState.update { value ->
                        value.copy(screenShotStatus = Error(STATUS_TEXT_RECOGNIZER_FAILED))
                    }
                }
        }
    }

    private fun String.checkTextAndFormat(): String {
        return replace("\n", " ")
            .replaceFirstChar { it.titlecase() }
            .ifBlank { ILLEGIBLE_TEXT }
    }

    private fun fetchPhraseToTranslate(text: String) {
        if (text != ILLEGIBLE_TEXT) {
            _uiState.update { it.copy(screenShotStatus = Loading) }
            viewModelScope.launch {
                try {
                    screenShotRepository.saveTranslationCount()
                    withContext(Dispatchers.IO) {
                        val requestBody = RequestBody(prompt = PROMPT_TRANSLATE(getLanguage(), text))
                        val response = screenShotRepository.getTranslatePhrase(requestBody)
                        val textResponse = response.choices?.get(0)?.text
                        _uiState.update { it.copy(screenShotStatus = Success(textResponse)) }
                    }
                } catch (e: HttpException) {
                    _uiState.update { it.copy(screenShotStatus = Error(e.code())) }

                } catch (e: Exception) {
                    _uiState.update { it.copy(screenShotStatus = Error(STATUS_TEXT_ERROR_GENERIC)) }
                }
            }
        } else {
            _uiState.update { it.copy(screenShotStatus = Success(text)) }
        }
    }

    private fun fetchLanguageIdentifier(text: String) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                fetchTextToSpeech(text, languageCode)
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
            viewModelScope.launch {
                _response.loading()
            }
        }

        override fun onDone(p0: String?) {
            viewModelScope.launch {
                _response.success(p0)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(p0: String?) {
            viewModelScope.launch {
                _response.error(STATUS_TEXT_TO_SPEECH_ERROR)
            }
        }
    }

    fun stopTextToSpeech() {
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

    fun getLanguageList(): List<String> {
        return enumValues<AvailableLanguage>()
            .toList()
            .sortedBy { it.displayName }
            .map { it.displayName }
    }

    fun hasReachedMaxTranslationCount(): Boolean {
        return screenShotRepository.hasReachedMaxTranslationCount()
    }

    companion object {
        private const val ILLEGIBLE_TEXT = "There isn't any legible text."
        private const val LANGUAGE_CODE_UNAVAILABLE = "und"
        private val PROMPT_TRANSLATE: (AvailableLanguage?, String) -> String = { language, text ->
            "Translate this into 1. ${language?.displayName}:\\n\\n${text}\\n\\n1."
        }
    }
}