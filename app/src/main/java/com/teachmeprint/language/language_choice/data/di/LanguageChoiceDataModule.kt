package com.teachmeprint.language.language_choice.data.di

import com.teachmeprint.language.language_choice.data.repository.LanguageChoiceRepositoryImpl
import com.teachmeprint.language.language_choice.data.storage.LanguageChoiceLocalStorage
import com.teachmeprint.language.language_choice.domain.repository.LanguageChoiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LanguageChoiceDataModule {

    @Singleton
    @Provides
    fun provideLanguageChoiceRepository(
        languageChoiceLocalStorage: LanguageChoiceLocalStorage,
    ): LanguageChoiceRepository =
        LanguageChoiceRepositoryImpl(languageChoiceLocalStorage)
}