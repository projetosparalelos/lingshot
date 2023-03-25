package com.language.teachermetoon.core.di.module

import com.language.teachermetoon.BuildConfig.BASE_API
import com.language.teachermetoon.BuildConfig.CHAT_GPT_KEY
import com.language.teachermetoon.data.remote.api.TranslateChatGPTService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiClientModule = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

val apiServiceModule = module {
    single { provideTranslateChatGPTService(get()) }
}

private fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
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

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

private fun provideTranslateChatGPTService(retrofit: Retrofit): TranslateChatGPTService =
    retrofit.create(TranslateChatGPTService::class.java)

private const val CONTENT_TYPE = "Content-Type"
private const val APPLICATION_JSON = "application/json"
private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer $CHAT_GPT_KEY"