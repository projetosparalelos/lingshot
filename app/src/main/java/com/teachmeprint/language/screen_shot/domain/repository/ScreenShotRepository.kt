package com.teachmeprint.language.screen_shot.domain.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text
import com.teachmeprint.language.core.data.model.chatgpt.RequestBody

interface ScreenShotRepository {
    suspend fun getTranslatePhrase(requestBody: RequestBody): String?

    fun fetchTextRecognizer(imageBitmap: Bitmap?): Task<Text>?

    fun fetchLanguageIdentifier(text: String): Task<String>
}