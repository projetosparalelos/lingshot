package com.lingshot.remote.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.lingshot.domain.repository.GoogleAuthRepository
import com.lingshot.remote.repository.GoogleAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {

    @Singleton
    @Provides
    fun provideGoogleAuthRepository(@ApplicationContext context: Context): GoogleAuthRepository =
        GoogleAuthRepositoryImpl(Identity.getSignInClient(context))
}
