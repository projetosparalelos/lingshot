package com.teachmeprint.language.core.di.module

import dagger.Module
import dagger.Provides
import com.teachmeprint.language.BuildConfig
import com.teachmeprint.language.data.remote.api.TranslateChatGPTService
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request =
                chain.request()
                    .newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .addHeader(AUTHORIZATION, BEARER)
                    .build()
            chain.proceed(newRequest)
        }.addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideTranslateChatGPTService(retrofit: Retrofit): TranslateChatGPTService =
        retrofit.create(TranslateChatGPTService::class.java)

    private const val CONTENT_TYPE = "Content-Type"
    private const val APPLICATION_JSON = "application/json"
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer ${BuildConfig.CHAT_GPT_KEY}"
}
