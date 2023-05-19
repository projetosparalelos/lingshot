package com.teachmeprint.language.screen_shot.data.di

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
import com.teachmeprint.language.core.data.remote.api.TranslateChatGPTService
import com.teachmeprint.language.screen_shot.data.repository.ScreenShotRepositoryImpl
import com.teachmeprint.language.screen_shot.domain.repository.ScreenShotRepository
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
        api: TranslateChatGPTService,
        textRecognizer: TextRecognizer,
        languageIdentifier: LanguageIdentifier
    ): ScreenShotRepository =
        ScreenShotRepositoryImpl(api, textRecognizer, languageIdentifier)

    @Singleton
    @Provides
    fun provideTextRecognition() =
        TextRecognition.getClient(DEFAULT_OPTIONS)

    @Singleton
    @Provides
    fun provideLanguageIdentification() = LanguageIdentification.getClient()
}