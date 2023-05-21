package com.teachmeprint.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teachmeprint.remote.BuildConfig
import dagger.Module
import dagger.Provides
import com.teachmeprint.remote.api.ChatGPTService
import com.teachmeprint.remote.repository.ChatGPTRepository
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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
        val contentType = "application/json".toMediaType()
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(BuildConfig.BASE_API)
            .build()
    }

    @Singleton
    @Provides
    fun provideChatGPTService(retrofit: Retrofit): ChatGPTService =
        retrofit.create(ChatGPTService::class.java)

    @Singleton
    @Provides
    fun provideChatGPTRepository(
        api: ChatGPTService,
    ): ChatGPTRepository = ChatGPTRepository(api)

    private const val CONTENT_TYPE = "Content-Type"
    private const val APPLICATION_JSON = "application/json"
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer ${BuildConfig.CHAT_GPT_KEY}"
}