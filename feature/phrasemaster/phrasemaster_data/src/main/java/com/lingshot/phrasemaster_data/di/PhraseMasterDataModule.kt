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
package com.lingshot.phrasemaster_data.di

import com.lingshot.domain.usecase.UserProfileUseCase
import com.lingshot.phrasemaster_data.repository.ConsecutiveDaysRepositoryImpl
import com.lingshot.phrasemaster_data.repository.PhraseCollectionRepositoryImpl
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhraseMasterDataModule {

    @Singleton
    @Provides
    fun providePhraseCollectionRepository(
        userProfileUseCase: UserProfileUseCase,
        languageCollectionMapper: LanguageCollectionMapper,
    ): PhraseCollectionRepository =
        PhraseCollectionRepositoryImpl(userProfileUseCase, languageCollectionMapper)

    @Singleton
    @Provides
    fun provideConsecutiveDaysRepository(
        userProfileUseCase: UserProfileUseCase,
    ): ConsecutiveDaysRepository =
        ConsecutiveDaysRepositoryImpl(userProfileUseCase)
}
