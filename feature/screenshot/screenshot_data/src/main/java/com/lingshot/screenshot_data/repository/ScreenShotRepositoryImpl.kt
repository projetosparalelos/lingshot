package com.lingshot.screenshot_data.repository

import android.graphics.Bitmap
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.lingshot.screenshot_domain.repository.ScreenShotRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ScreenShotRepositoryImpl @Inject constructor(
    private val textRecognizer: TextRecognizer,
    private val languageIdentifier: LanguageIdentifier
) : ScreenShotRepository {

    override suspend fun fetchTextRecognizer(imageBitmap: Bitmap?): Status<String> {
        return try {
            val inputImage = imageBitmap?.let { InputImage.fromBitmap(it, 0) }
            val text = inputImage?.let { textRecognizer.process(it) }?.await()?.text
            statusSuccess(text)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun fetchLanguageIdentifier(text: String): Status<String> {
        return try {
            val code = languageIdentifier.identifyLanguage(text).await()
            statusSuccess(code)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }
}
