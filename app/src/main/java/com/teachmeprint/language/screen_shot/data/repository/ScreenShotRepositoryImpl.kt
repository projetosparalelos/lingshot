package com.teachmeprint.language.screen_shot.data.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.teachmeprint.language.core.data.model.chatgpt.RequestBody
import com.teachmeprint.language.core.data.remote.api.TranslateChatGPTService
import com.teachmeprint.language.screen_shot.domain.repository.ScreenShotRepository
import javax.inject.Inject

class ScreenShotRepositoryImpl @Inject constructor(
    private val translateChatGPTService: TranslateChatGPTService,
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
): ScreenShotRepository {

    override suspend fun getTranslatePhrase(requestBody: RequestBody): String? {
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
}