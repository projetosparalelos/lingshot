package com.teachmeprint.common.di

import com.teachmeprint.common.helper.MobileAdsFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideMobileAdsFacade() = MobileAdsFacade()
}
