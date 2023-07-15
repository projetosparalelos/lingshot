package com.lingshot.screencapture.di

import android.content.Context
import com.lingshot.screencapture.ScreenCaptureFloatingWindow
import com.lingshot.screencapture.helper.ScreenCaptureFloatingWindowLifecycle
import com.lingshot.screencapture.helper.ScreenCaptureManager
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

    @Singleton
    @Provides
    fun provideScreenCaptureFloatingWindowLifecycle(
        screenCaptureFloatingWindow: ScreenCaptureFloatingWindow
    ) = ScreenCaptureFloatingWindowLifecycle(screenCaptureFloatingWindow)
}
