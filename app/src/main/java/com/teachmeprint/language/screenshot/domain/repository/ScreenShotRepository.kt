package com.teachmeprint.language.screenshot.domain.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text

interface ScreenShotRepository {
    suspend fun getTranslatePhrase(text: String): String?

    fun fetchTextRecognizer(imageBitmap: Bitmap?): Task<Text>?

    fun fetchLanguageIdentifier(text: String): Task<String>
}