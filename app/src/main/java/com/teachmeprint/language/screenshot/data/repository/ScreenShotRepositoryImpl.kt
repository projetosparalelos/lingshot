package com.teachmeprint.language.screenshot.data.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.teachmeprint.language.data.local.storage.LanguageLocalStorage
import com.teachmeprint.language.data.local.storage.TranslationCountLocalStore
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.data.model.screenshot.entity.RequestBody
import com.teachmeprint.language.data.remote.api.TranslateChatGPTService
import com.teachmeprint.language.screenshot.domain.repository.ScreenShotRepository
import javax.inject.Inject

class ScreenShotRepositoryImpl @Inject constructor(
    private val translateChatGPTService: TranslateChatGPTService,
    private val languageLocalStorage: LanguageLocalStorage,
    private val translationCountLocalStore: TranslationCountLocalStore,
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
): ScreenShotRepository {

    override suspend fun getTranslatePhrase(text: String): String? {
        val requestBody = RequestBody(prompt = PROMPT_TRANSLATE(getLanguage(), text))
        val response = translateChatGPTService.getTranslatePhrase(requestBody)
        return response.choices?.get(0)?.text
    }

    override fun fetchTextRecognizer(imageBitmap: Bitmap?): Task<Text>? {
        val inputImage = imageBitmap?.let { InputImage.fromBitmap(it, 0) }
        return inputImage?.let { textRecognizer.process(it) }
    }

    override fun fetchLanguageIdentifier(text: String): Task<String> {
        return languageIdentifier.identifyLanguage(text)
    }

    fun getLanguage(): AvailableLanguage? {
        return languageLocalStorage.getLanguage()
    }

    fun saveLanguage(availableLanguage: AvailableLanguage?) {
        languageLocalStorage.saveLanguage(availableLanguage)
    }

    fun saveTranslationCount() {
        translationCountLocalStore.saveTranslationCount()
    }

    fun hasReachedMaxTranslationCount(): Boolean {
        return translationCountLocalStore.hasReachedMaxTranslationCount()
    }

    companion object {
        private val PROMPT_TRANSLATE: (AvailableLanguage?, String) -> String = { language, text ->
            "Translate this into 1. ${language?.displayName}:\\n\\n${text}\\n\\n1."
        }
    }
}