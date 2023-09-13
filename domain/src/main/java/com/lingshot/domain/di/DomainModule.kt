package com.lingshot.domain.di

import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.GoogleAuthRepository
import com.lingshot.domain.repository.TextIdentifierRepository
import com.lingshot.domain.repository.UserLocalRepository
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.domain.usecase.RetrieveGoalsUseCase
import com.lingshot.domain.usecase.SaveGoalsUseCase
import com.lingshot.domain.usecase.SavePhrasesCompletedGoalsUseCase
import com.lingshot.domain.usecase.SignOutUseCase
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

    @Singleton
    @Provides
    fun provideSignOutUseCase(
        googleAuthRepository: GoogleAuthRepository
    ): SignOutUseCase = SignOutUseCase(googleAuthRepository)

    @Singleton
    @Provides
    fun provideLanguageIdentifierUseCase(
        textIdentifierRepository: TextIdentifierRepository
    ): LanguageIdentifierUseCase = LanguageIdentifierUseCase(textIdentifierRepository)

    @Singleton
    @Provides
    fun provideRetrieveGoalsUseCase(
        goalsRepository: GoalsRepository,
        userLocalRepository: UserLocalRepository,
        userProfileUseCase: UserProfileUseCase
    ): RetrieveGoalsUseCase = RetrieveGoalsUseCase(
        goalsRepository,
        userLocalRepository,
        userProfileUseCase
    )

    @Singleton
    @Provides
    fun provideSaveGoalsUseCase(
        goalsRepository: GoalsRepository,
        userLocalRepository: UserLocalRepository,
        userProfileUseCase: UserProfileUseCase
    ): SaveGoalsUseCase = SaveGoalsUseCase(goalsRepository, userLocalRepository, userProfileUseCase)

    @Singleton
    @Provides
    fun provideSavePhrasesCompletedGoalsUseCase(
        goalsRepository: GoalsRepository,
        userLocalRepository: UserLocalRepository,
        userProfileUseCase: UserProfileUseCase
    ): SavePhrasesCompletedGoalsUseCase =
        SavePhrasesCompletedGoalsUseCase(goalsRepository, userLocalRepository, userProfileUseCase)
}
