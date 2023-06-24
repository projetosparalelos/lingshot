package com.teachmeprint.remote.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.teachmeprint.domain.repository.GoogleAuthRepository
import com.teachmeprint.remote.repository.GoogleAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideGoogleAuthRepository(@ApplicationContext context: Context): GoogleAuthRepository =
        GoogleAuthRepositoryImpl(Identity.getSignInClient(context))
}