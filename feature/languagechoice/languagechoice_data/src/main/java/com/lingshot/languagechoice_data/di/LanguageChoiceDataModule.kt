package com.lingshot.languagechoice_data.di

import com.lingshot.languagechoice_data.repository.LanguageChoiceRepositoryImpl
import com.lingshot.languagechoice_data.storage.LanguageChoiceLocalStorage
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
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
        languageChoiceLocalStorage: LanguageChoiceLocalStorage
    ): LanguageChoiceRepository =
        LanguageChoiceRepositoryImpl(languageChoiceLocalStorage)
}
