/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lingshot.remote.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.domain.repository.GoogleTranslateRepository
import com.lingshot.remote.BuildConfig
import com.lingshot.remote.api.ChatGPTService
import com.lingshot.remote.api.GoogleTranslateService
import com.lingshot.remote.repository.ChatGPTRepositoryImpl
import com.lingshot.remote.repository.GoogleTranslateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ) = ChuckerInterceptor.Builder(context).build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request =
                chain.request()
                    .newBuilder()
                    .build()
            chain.proceed(newRequest)
        }.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            addInterceptor(chuckerInterceptor)
        }.readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideChatGPTService(retrofit: Retrofit.Builder): ChatGPTService =
        retrofit.baseUrl(BuildConfig.BASE_CHAT_GPT_API).build()
            .create(ChatGPTService::class.java)

    @Singleton
    @Provides
    fun provideChatGPTRepository(
        api: ChatGPTService,
    ): ChatGPTRepository = ChatGPTRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideGoogleTranslateService(retrofit: Retrofit.Builder): GoogleTranslateService =
        retrofit.baseUrl(BuildConfig.BASE_GOOGLE_TRANSLATE_API).build()
            .create(GoogleTranslateService::class.java)

    @Singleton
    @Provides
    fun provideGoogleTranslateRepository(
        api: GoogleTranslateService,
    ): GoogleTranslateRepository = GoogleTranslateRepositoryImpl(api)
}
