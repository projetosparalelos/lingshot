package com.phrase.phrasemaster_domain.di

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.mapper.LanguageCodeFromAndToMapper
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import com.phrase.phrasemaster_domain.usecase.SavePhraseLanguageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhraseMasterDomainModule {

    @Singleton
    @Provides
    fun provideSavePhraseLanguageUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
        languageIdentifierUseCase: LanguageIdentifierUseCase
    ): SavePhraseLanguageUseCase = SavePhraseLanguageUseCase(
        phraseCollectionRepository,
        languageIdentifierUseCase
    )

    @Singleton
    @Provides
    fun provideLanguageCodeFromAndToMapper(
        languageChoiceRepository: LanguageChoiceRepository,
        languageIdentifierUseCase: LanguageIdentifierUseCase
    ): LanguageCodeFromAndToMapper = LanguageCodeFromAndToMapper(
        languageChoiceRepository,
        languageIdentifierUseCase
    )
}
