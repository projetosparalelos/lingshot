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
package lingshot.teachmeprint.local.di

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
import com.lingshot.domain.repository.TextIdentifierRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lingshot.teachmeprint.local.repository.TextIdentifierRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideScreenShotRepository(
        textRecognizer: TextRecognizer,
        languageIdentifier: LanguageIdentifier,
    ): TextIdentifierRepository =
        TextIdentifierRepositoryImpl(textRecognizer, languageIdentifier)

    @Singleton
    @Provides
    fun provideTextRecognition() =
        TextRecognition.getClient(DEFAULT_OPTIONS)

    @Singleton
    @Provides
    fun provideLanguageIdentification() = LanguageIdentification.getClient()
}
