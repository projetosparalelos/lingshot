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
package com.lingshot.common.helper

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_MISSING_DATA
import android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import android.speech.tts.TextToSpeech.SUCCESS
import android.speech.tts.UtteranceProgressListener
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusLoading
import com.lingshot.domain.model.statusSuccess
import java.util.Locale

class TextToSpeechFacade<T>(
    private val context: Context,
    private val speechStatusCallback: (Status<T>) -> Unit,
) {

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != SUCCESS) {
                speechStatusCallback(statusError(STATUS_TEXT_TO_SPEECH_FAILED))
            }
        }
    }

    init {
        setupTextToSpeech()
    }

    fun speakText(text: String, languageCode: String) =
        with(textToSpeech) {
            val languageLocale = if (languageCode == LANGUAGE_CODE_UNAVAILABLE) {
                Locale.US
            } else {
                Locale.forLanguageTag(languageCode)
            }

            val result = setLanguage(languageLocale)
            if (result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED) {
                speechStatusCallback(statusError(STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED))
            }

            speak(text, QUEUE_FLUSH, null, "")
        }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun setupTextToSpeech() {
        textToSpeech.setOnUtteranceProgressListener(onSpeechListener())
        textToSpeech.setSpeechRate(0.7f)
    }

    private fun onSpeechListener() = object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            speechStatusCallback(statusLoading())
        }

        override fun onDone(utteranceId: String?) {
            speechStatusCallback(statusSuccess(null))
        }

        @Deprecated("Deprecated in Java", ReplaceWith("", ""))
        override fun onError(utteranceId: String?) {
            speechStatusCallback(statusError(utteranceId))
        }
    }

    companion object {
        private const val LANGUAGE_CODE_UNAVAILABLE = "und"
        private const val STATUS_TEXT_TO_SPEECH_FAILED = "Text to speech failed."
        private const val STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED = "Text to speech not supported."
    }
}
