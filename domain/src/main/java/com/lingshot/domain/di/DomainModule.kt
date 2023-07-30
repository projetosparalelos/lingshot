package com.lingshot.domain.di

import com.lingshot.domain.repository.GoogleAuthRepository
import com.lingshot.domain.usecase.UserProfileUseCase
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
}
