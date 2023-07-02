package com.teachmeprint.domain.di

import com.teachmeprint.domain.repository.GoogleAuthRepository
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import com.teachmeprint.domain.usecase.SavePhraseLanguageUseCase
import com.teachmeprint.domain.usecase.UserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideUserProfileUseCase(
        googleAuthRepository: GoogleAuthRepository
    ): UserProfileUseCase = UserProfileUseCase(googleAuthRepository)

    @Singleton
    @Provides
    fun provideSavePhraseLanguageUseCase(
        phraseCollectionRepository: PhraseCollectionRepository
    ): SavePhraseLanguageUseCase = SavePhraseLanguageUseCase(phraseCollectionRepository)
}