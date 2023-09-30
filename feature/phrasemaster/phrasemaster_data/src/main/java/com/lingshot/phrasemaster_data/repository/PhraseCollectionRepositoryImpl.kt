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
package com.lingshot.phrasemaster_data.repository

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.model.CollectionInfoDomain
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.model.encodeId
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhraseCollectionRepositoryImpl @Inject constructor(
    private val useCase: UserProfileUseCase,
    private val languageCollectionMapper: LanguageCollectionMapper,
) : PhraseCollectionRepository {

    private val db = Firebase.firestore

    private val queryCollectionByLanguages
        get() =
            db.collection(COLLECTION_DECKS)
                .document(useCase()?.userId.toString())
                .collection(COLLECTION_LANGUAGES)

    override suspend fun savePhraseInLanguageCollections(
        phraseDomain: PhraseDomain,
    ) {
        val languageCollectionDomain = languageCollectionMapper(phraseDomain.original)
        queryCollectionByLanguages
            .document(languageCollectionDomain.id)
            .apply { set(languageCollectionDomain) }
            .collection(COLLECTION_PHRASES)
            .document(phraseDomain.id)
            .set(phraseDomain)
            .await()
    }

    override suspend fun updatePhraseInLanguageCollections(
        languageId: String,
        phraseDomain: PhraseDomain,
    ) {
        queryCollectionByLanguages
            .document(languageId)
            .collection(COLLECTION_PHRASES)
            .document(phraseDomain.id)
            .set(phraseDomain)
            .await()
    }

    override suspend fun getLanguageCollections(): Pair<List<LanguageCollectionDomain>, CollectionInfoDomain> {
        val queryResult = queryCollectionByLanguages.get().await()

        val languageCollections = queryResult.map {
            it.toObject(LanguageCollectionDomain::class.java)
        }

        val listTotalPhrases = queryResult.map { languageDoc ->
            languageDoc
                .reference
                .collection(COLLECTION_PHRASES)
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count
                .toInt()
        }

        val listPhrasesPlayed = queryResult.map { languageDoc ->
            languageDoc
                .reference
                .collection(COLLECTION_PHRASES)
                .whereGreaterThan("reviewLevel", 0)
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count
                .toInt()
        }

        val collectionInfo = CollectionInfoDomain(listTotalPhrases, listPhrasesPlayed)

        return languageCollections to collectionInfo
    }

    override suspend fun getPhrasesForNextReview(
        languageId: String,
    ): List<PhraseDomain> {
        return queryCollectionByLanguages
            .document(languageId)
            .collection(COLLECTION_PHRASES)
            .whereLessThanOrEqualTo("nextReviewTimestamp", System.currentTimeMillis())
            .get()
            .await().map {
                it.toObject(PhraseDomain::class.java)
            }
    }

    override suspend fun getPhrasesPendingReview(): String {
        return queryCollectionByLanguages.get().await()
            .flatMap { language ->
                val phrases = language.reference.collection(COLLECTION_PHRASES)

                val phrasesWithTimestampFilter = phrases
                    .whereLessThanOrEqualTo("nextReviewTimestamp", System.currentTimeMillis())
                    .get()
                    .await()

                val phrasesWithReviewLevelFilter = phrases
                    .whereGreaterThan("reviewLevel", 0)
                    .get()
                    .await()

                phrasesWithTimestampFilter.intersect(phrasesWithReviewLevelFilter.toSet())
            }.size.toString()
    }

    override suspend fun isPhraseSaved(originalText: String): Boolean {
        val languageId = languageCollectionMapper(originalText).id
        val phraseId = originalText.encodeId()
        return queryCollectionByLanguages
            .document(languageId)
            .collection(COLLECTION_PHRASES)
            .document(phraseId)
            .get().await().exists()
    }

    override suspend fun deletePhraseSaved(originalText: String) {
        val languageId = languageCollectionMapper(originalText).id
        val phraseId = originalText.encodeId()
        queryCollectionByLanguages
            .document(languageId)
            .collection(COLLECTION_PHRASES)
            .document(phraseId)
            .delete().await()
    }

    companion object {
        private const val COLLECTION_DECKS = "decks"
        private const val COLLECTION_LANGUAGES = "languages"
        private const val COLLECTION_PHRASES = "phrases"
    }
}
