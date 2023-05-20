package com.teachmeprint.screenshot_domain.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text

interface ScreenShotRepository {

    fun fetchTextRecognizer(imageBitmap: Bitmap?): Task<Text>?

    fun fetchLanguageIdentifier(text: String): Task<String>
}