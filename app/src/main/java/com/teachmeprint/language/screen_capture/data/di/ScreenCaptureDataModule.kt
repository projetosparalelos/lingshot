package com.teachmeprint.language.screen_capture.data.di

import android.content.Context
import com.teachmeprint.language.screen_capture.data.helper.ScreenCaptureManager
import com.teachmeprint.language.screen_capture.presentation.ui.ScreenCaptureFloatingWindow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScreenCaptureDataModule {

    @Singleton
    @Provides
    fun provideScreenShotFloatingWindow(@ApplicationContext context: Context) =
        ScreenCaptureFloatingWindow(context)

    @Singleton
    @Provides
    fun provideScreenCaptureManager(
        @ApplicationContext context: Context
    ) = ScreenCaptureManager(context)
}