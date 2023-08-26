package com.phrase.phrasemaster_domain.di

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import com.phrase.phrasemaster_domain.usecase.CheckSavedPhraseUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesForNextReviewUseCase
import com.phrase.phrasemaster_domain.usecase.SavePhraseLanguageUseCase
import com.phrase.phrasemaster_domain.usecase.UpdatePhraseReviewUseCase
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
    fun provideCheckSavedPhraseUseCase(
        phraseCollectionRepository: PhraseCollectionRepository
    ): CheckSavedPhraseUseCase = CheckSavedPhraseUseCase(
        phraseCollectionRepository
    )

    @Singleton
    @Provides
    fun provideRetrieveLanguageCollectionsUseCase(
        phraseCollectionRepository: PhraseCollectionRepository
    ): RetrieveLanguageCollectionsUseCase = RetrieveLanguageCollectionsUseCase(
        phraseCollectionRepository
    )

    @Singleton
    @Provides
    fun provideRetrievePhrasesForNextReviewUseCase(
        phraseCollectionRepository: PhraseCollectionRepository
    ): RetrievePhrasesForNextReviewUseCase = RetrievePhrasesForNextReviewUseCase(
        phraseCollectionRepository
    )

    @Singleton
    @Provides
    fun provideUpdatePhraseReviewUseCase(
        phraseCollectionRepository: PhraseCollectionRepository
    ): UpdatePhraseReviewUseCase = UpdatePhraseReviewUseCase(
        phraseCollectionRepository
    )

    @Singleton
    @Provides
    fun provideLanguageCollectionMapper(
        languageChoiceRepository: LanguageChoiceRepository,
        languageIdentifierUseCase: LanguageIdentifierUseCase
    ): LanguageCollectionMapper = LanguageCollectionMapper(
        languageChoiceRepository,
        languageIdentifierUseCase
    )
}
