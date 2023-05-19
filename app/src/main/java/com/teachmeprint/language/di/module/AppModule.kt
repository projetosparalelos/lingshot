package com.teachmeprint.language.di.module

import android.content.Context
import com.teachmeprint.language.core.common.helper.MobileAdsFacade
import com.teachmeprint.language.core.common.helper.NotificationClearScreenshot
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotificationClearScreenshot(@ApplicationContext context: Context) =
        NotificationClearScreenshot(context)

    @Singleton
    @Provides
    fun provideMobileAdsFacade() = MobileAdsFacade()
}