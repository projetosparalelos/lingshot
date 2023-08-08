package com.lingshot.phrasemaster_data.di

import com.lingshot.domain.usecase.UserProfileUseCase
import com.lingshot.phrasemaster_data.repository.PhraseCollectionRepositoryImpl
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhraseMasterDataModule {

    @Singleton
    @Provides
    fun providePhraseCollectionRepository(
        userProfileUseCase: UserProfileUseCase,
        languageCollectionMapper: LanguageCollectionMapper
    ): PhraseCollectionRepository =
        PhraseCollectionRepositoryImpl(userProfileUseCase, languageCollectionMapper)
}
