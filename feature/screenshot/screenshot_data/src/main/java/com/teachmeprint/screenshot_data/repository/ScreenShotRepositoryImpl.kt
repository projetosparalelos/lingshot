package com.teachmeprint.screenshot_data.repository

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.teachmeprint.screenshot_domain.repository.ScreenShotRepository
import javax.inject.Inject

class ScreenShotRepositoryImpl @Inject constructor(
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
) : ScreenShotRepository {

    override fun fetchTextRecognizer(imageBitmap: Bitmap?): Task<Text>? {
        val inputImage = imageBitmap?.let { InputImage.fromBitmap(it, 0) }
        return inputImage?.let { textRecognizer.process(it) }
    }

    override fun fetchLanguageIdentifier(text: String): Task<String> {
        return languageIdentifier.identifyLanguage(text)
    }
}
