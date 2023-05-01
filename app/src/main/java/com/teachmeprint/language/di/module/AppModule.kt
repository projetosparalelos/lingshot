package com.teachmeprint.language.di.module

import android.content.Context
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
import com.teachmeprint.language.core.common.helper.MobileAdsFacade
import com.teachmeprint.language.core.common.helper.NotificationClearScreenshot
import com.teachmeprint.language.core.common.helper.ScreenCaptureManager
import dagger.Module
import dagger.Provides
import com.teachmeprint.language.data.local.storage.LanguageLocalStorage
import com.teachmeprint.language.data.local.storage.TranslationCountLocalStore
import com.teachmeprint.language.data.remote.api.TranslateChatGPTService
import com.teachmeprint.language.presentation.screenshot.ui.ScreenShotFloatingWindow
import com.teachmeprint.language.data.repository.ScreenShotRepository
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideScreenShotRepository(
        api: TranslateChatGPTService,
        languageLocalStorage: LanguageLocalStorage,
        translationCountLocalStore: TranslationCountLocalStore,
        textRecognizer: TextRecognizer,
        languageIdentifier: LanguageIdentifier
    ) = ScreenShotRepository(
        api, languageLocalStorage,
        translationCountLocalStore, textRecognizer, languageIdentifier
    )

    @Singleton
    @Provides
    fun provideTextRecognition() =
        TextRecognition.getClient(DEFAULT_OPTIONS)

    @Singleton
    @Provides
    fun provideLanguageIdentification() = LanguageIdentification.getClient()

    @Singleton
    @Provides
    fun provideScreenShotFloatingWindow(@ApplicationContext context: Context) =
        ScreenShotFloatingWindow(context)

    @Singleton
    @Provides
    fun provideNotificationClearScreenshot(@ApplicationContext context: Context) =
        NotificationClearScreenshot(context)

    @Singleton
    @Provides
    fun provideScreenCaptureManager(
        @ApplicationContext context: Context
    ) = ScreenCaptureManager(context)

    @Singleton
    @Provides
    fun provideMobileAdsFacade() = MobileAdsFacade()
}