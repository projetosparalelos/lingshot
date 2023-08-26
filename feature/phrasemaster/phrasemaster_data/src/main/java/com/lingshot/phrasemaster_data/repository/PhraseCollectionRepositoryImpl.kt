package com.lingshot.phrasemaster_data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.model.encodeId
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class PhraseCollectionRepositoryImpl @Inject constructor(
    private val useCase: UserProfileUseCase,
    private val languageCollectionMapper: LanguageCollectionMapper
) : PhraseCollectionRepository {

    private val db = Firebase.firestore

    private val queryCollectionByLanguages
        get() =
            db.collection(COLLECTION_DECKS)
                .document(useCase()?.userId.toString())
                .collection(COLLECTION_LANGUAGES)

    override suspend fun savePhraseInLanguageCollections(
        phraseDomain: PhraseDomain
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
        phraseDomain: PhraseDomain
    ) {
        queryCollectionByLanguages
            .document(languageId)
            .collection(COLLECTION_PHRASES)
            .document(phraseDomain.id)
            .set(phraseDomain)
            .await()
    }

    override suspend fun getLanguageCollections(): List<LanguageCollectionDomain> {
        return queryCollectionByLanguages
            .get()
            .await().map {
                it.toObject(
                    LanguageCollectionDomain::class.java
                )
            }
    }

    override suspend fun getPhrasesForNextReview(
        languageId: String
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
