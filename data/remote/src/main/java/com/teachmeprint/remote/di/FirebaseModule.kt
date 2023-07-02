package com.teachmeprint.remote.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.teachmeprint.domain.repository.GoogleAuthRepository
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import com.teachmeprint.domain.usecase.UserProfileUseCase
import com.teachmeprint.remote.repository.GoogleAuthRepositoryImpl
import com.teachmeprint.remote.repository.PhraseCollectionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideGoogleAuthRepository(@ApplicationContext context: Context): GoogleAuthRepository =
        GoogleAuthRepositoryImpl(Identity.getSignInClient(context))

    @Singleton
    @Provides
    fun providePhraseCollectionRepository(userProfileUseCase: UserProfileUseCase):
        PhraseCollectionRepository = PhraseCollectionRepositoryImpl(userProfileUseCase)
}