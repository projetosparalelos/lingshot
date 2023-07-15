package com.lingshot.screenshot_domain.repository

import android.graphics.Bitmap
import com.lingshot.domain.model.Status

interface ScreenShotRepository {

    suspend fun fetchTextRecognizer(imageBitmap: Bitmap?): Status<String>

    suspend fun fetchLanguageIdentifier(text: String): Status<String>
}
