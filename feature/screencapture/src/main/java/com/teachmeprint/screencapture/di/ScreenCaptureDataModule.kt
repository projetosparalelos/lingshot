package com.teachmeprint.screencapture.di

import android.content.Context
import com.teachmeprint.screencapture.ScreenCaptureFloatingWindow
import com.teachmeprint.screencapture.helper.ScreenCaptureManager
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
