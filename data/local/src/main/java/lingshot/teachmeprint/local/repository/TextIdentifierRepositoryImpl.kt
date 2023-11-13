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
package lingshot.teachmeprint.local.repository

import android.graphics.Bitmap
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.lingshot.domain.repository.TextIdentifierRepository
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.FROM
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class TextIdentifierRepositoryImpl @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val languageIdentifier: LanguageIdentifier,
) : TextIdentifierRepository {

    override suspend fun fetchTextRecognizer(imageBitmap: Bitmap?): Status<String> {
        return try {
            val inputImage = imageBitmap?.let { InputImage.fromBitmap(it, 0) }
            val text = inputImage?.let { textRecognizer().process(it) }?.await()?.text
            statusSuccess(text)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    private suspend fun textRecognizer(): TextRecognizer {
        val provideDefault = TextRecognizerOptions.Builder().build()
        val provideChinese = ChineseTextRecognizerOptions.Builder().build()
        val provideJapanese = JapaneseTextRecognizerOptions.Builder().build()
        val provideKorean = KoreanTextRecognizerOptions.Builder().build()

        val textRecognizer = when (languageChoiceRepository.getLanguage(FROM).first()) {
            AvailableLanguage.CHINESE -> provideChinese
            AvailableLanguage.JAPANESE -> provideJapanese
            AvailableLanguage.KOREAN -> provideKorean
            else -> provideDefault
        }

        return TextRecognition.getClient(textRecognizer)
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
