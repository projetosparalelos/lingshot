package com.language.teachermetoon.feature.screenshot.presentation

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
import com.language.teachermetoon.core.helper.*
import com.language.teachermetoon.core.helper.StatusMessage.STATUS_IDENTIFY_LANGUAGE_FAILED
import com.language.teachermetoon.core.helper.StatusMessage.STATUS_TEXT_RECOGNIZER_FAILED
import com.language.teachermetoon.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_ERROR
import com.language.teachermetoon.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_FAILED
import com.language.teachermetoon.core.helper.StatusMessage.STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum.LISTEN
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum.TRANSLATE
import com.language.teachermetoon.data.model.screenshot.entity.RequestBody
import com.language.teachermetoon.feature.screenshot.repository.ScreenShotRepository
import kotlinx.coroutines.launch
import java.util.*

class ScreenShotViewModel(
    context: Context,
    private val screenShotRepository: ScreenShotRepository,
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
) : ViewModel() {

    private val _response = MutableLiveResource<String>()
    val response by getLiveData(_response)

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != SUCCESS) {
                _response.error(STATUS_TEXT_TO_SPEECH_FAILED)
            }
        }
    }

    lateinit var typeActionEnum: TypeActionEnum

    init {
        setupTextToSpeech()
    }

    fun fetchTextRecognizer(imageBitmap: Bitmap?) {
        val inputImage = imageBitmap?.let { fromBitmap(it, 0) }
        inputImage?.let {
            textRecognizer.process(it)
                .addOnSuccessListener { value ->
                    val textFormatted = value.text.checkTextAndFormat()
                    when (typeActionEnum) {
                        TRANSLATE -> fetchPhraseToTranslate(textFormatted)
                        LISTEN -> fetchLanguageIdentifier(textFormatted)
                    }
                }
                .addOnFailureListener {
                    _response.error(STATUS_TEXT_RECOGNIZER_FAILED)
                }
        }
    }

    private fun String.checkTextAndFormat(): String {
        val textFormatted = this.replace("\n", " ")
        return textFormatted.ifBlank { ILLEGIBLE_TEXT }
    }

    private fun fetchPhraseToTranslate(text: String) {
        if (text != ILLEGIBLE_TEXT) {
            _response.loading()
            viewModelScope.launchResource(_response, {
                val requestBody = RequestBody(prompt = PROMPT_TRANSLATE(getLanguage(), text))
                val response = screenShotRepository.getTranslatePhrase(requestBody)

                response.choices[0].text
            })
        } else {
            _response.success(text)
        }
    }

    private fun fetchLanguageIdentifier(text: String) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                fetchTextToSpeech(text, languageCode)
            }
            .addOnFailureListener {
                _response.error(STATUS_IDENTIFY_LANGUAGE_FAILED)
            }
    }

    private fun fetchTextToSpeech(text: String, languageCode: String) =
        with(textToSpeech) {
            val languageLocale = if (languageCode == LANGUAGE_CODE_UNAVAILABLE) Locale.US
            else Locale.forLanguageTag(languageCode)

            val result = setLanguage(languageLocale)
            if (result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED) {
                _response.error(STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED)
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

    fun getLanguage(): String? {
        return screenShotRepository.getLanguage()
    }

    fun saveLanguage(languageSelected: String) {
        screenShotRepository.saveLanguage(languageSelected)
    }

    companion object {
        private const val ILLEGIBLE_TEXT = "There isn't any legible text."
        private const val LANGUAGE_CODE_UNAVAILABLE = "und"
        private val PROMPT_TRANSLATE: (String?, String) -> String = { language, text ->
            "Translate this into 1. ${language}:\\n\\n${text}\\n\\n1."
        }
    }
}