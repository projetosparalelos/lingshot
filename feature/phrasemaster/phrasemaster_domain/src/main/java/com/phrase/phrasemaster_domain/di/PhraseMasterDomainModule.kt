package com.phrase.phrasemaster_domain.di

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
        phraseCollectionRepository: PhraseCollectionRepository
    ): SavePhraseLanguageUseCase = SavePhraseLanguageUseCase(phraseCollectionRepository)
}
