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
package com.phrase.phrasemaster_domain.di

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import com.phrase.phrasemaster_domain.usecase.CheckSavedPhraseUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveConsecutiveDaysUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesForNextReviewUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesPendingReviewUseCase
import com.phrase.phrasemaster_domain.usecase.SavePhraseLanguageUseCase
import com.phrase.phrasemaster_domain.usecase.UpdateConsecutiveDaysUseCase
import com.phrase.phrasemaster_domain.usecase.UpdatePhraseReviewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhraseMasterDomainModule {

    @Singleton
    @Provides
    fun provideSavePhraseLanguageUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
        languageIdentifierUseCase: LanguageIdentifierUseCase,
    ): SavePhraseLanguageUseCase = SavePhraseLanguageUseCase(
        phraseCollectionRepository,
        languageIdentifierUseCase,
    )

    @Singleton
    @Provides
    fun provideCheckSavedPhraseUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
    ): CheckSavedPhraseUseCase = CheckSavedPhraseUseCase(
        phraseCollectionRepository,
    )

    @Singleton
    @Provides
    fun provideRetrieveLanguageCollectionsUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
    ): RetrieveLanguageCollectionsUseCase = RetrieveLanguageCollectionsUseCase(
        phraseCollectionRepository,
    )

    @Singleton
    @Provides
    fun provideRetrievePhrasesForNextReviewUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
    ): RetrievePhrasesForNextReviewUseCase = RetrievePhrasesForNextReviewUseCase(
        phraseCollectionRepository,
    )

    @Singleton
    @Provides
    fun provideRetrievePhrasesPendingReviewUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
    ): RetrievePhrasesPendingReviewUseCase = RetrievePhrasesPendingReviewUseCase(
        phraseCollectionRepository,
    )

    @Singleton
    @Provides
    fun provideUpdatePhraseReviewUseCase(
        phraseCollectionRepository: PhraseCollectionRepository,
    ): UpdatePhraseReviewUseCase = UpdatePhraseReviewUseCase(
        phraseCollectionRepository,
    )

    @Singleton
    @Provides
    fun provideLanguageCollectionMapper(
        languageChoiceRepository: LanguageChoiceRepository,
        languageIdentifierUseCase: LanguageIdentifierUseCase,
    ): LanguageCollectionMapper = LanguageCollectionMapper(
        languageChoiceRepository,
        languageIdentifierUseCase,
    )

    @Singleton
    @Provides
    fun provideRetrieveConsecutiveDaysUseCase(
        consecutiveDaysRepository: ConsecutiveDaysRepository,
    ): RetrieveConsecutiveDaysUseCase = RetrieveConsecutiveDaysUseCase(
        consecutiveDaysRepository,
    )

    @Singleton
    @Provides
    fun provideUpdateConsecutiveDaysUseCase(
        consecutiveDaysRepository: ConsecutiveDaysRepository,
    ): UpdateConsecutiveDaysUseCase = UpdateConsecutiveDaysUseCase(
        consecutiveDaysRepository,
    )
}
