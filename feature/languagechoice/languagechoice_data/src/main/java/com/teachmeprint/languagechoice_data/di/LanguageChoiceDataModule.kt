package com.teachmeprint.languagechoice_data.di

import com.teachmeprint.languagechoice_data.repository.LanguageChoiceRepositoryImpl
import com.teachmeprint.languagechoice_data.storage.LanguageChoiceLocalStorage
import com.teachmeprint.languagechoice_domain.repository.LanguageChoiceRepository
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