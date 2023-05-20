package com.teachmeprint.screenshot_data.di

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
import com.teachmeprint.screenshot_data.repository.ScreenShotRepositoryImpl
import com.teachmeprint.screenshot_domain.repository.ScreenShotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScreenShotDataModule {

    @Singleton
    @Provides
    fun provideScreenShotRepository(
        textRecognizer: TextRecognizer,
        languageIdentifier: LanguageIdentifier
    ): ScreenShotRepository =
        ScreenShotRepositoryImpl(textRecognizer, languageIdentifier)

    @Singleton
    @Provides
    fun provideTextRecognition() =
        TextRecognition.getClient(DEFAULT_OPTIONS)

    @Singleton
    @Provides
    fun provideLanguageIdentification() = LanguageIdentification.getClient()
}