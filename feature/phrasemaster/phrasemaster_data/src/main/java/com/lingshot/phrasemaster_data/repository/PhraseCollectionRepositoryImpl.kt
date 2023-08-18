package com.lingshot.phrasemaster_data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.mapper.LanguageCollectionMapper
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.model.encodeId
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await
import timber.log.Timber

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
    ): Status<Unit> {
        val languageCollectionDomain = languageCollectionMapper(phraseDomain.original)
        return try {
            queryCollectionByLanguages
                .document(languageCollectionDomain.id)
                .apply { set(languageCollectionDomain) }
                .collection(COLLECTION_PHRASES)
                .document(phraseDomain.id)
                .set(phraseDomain)
                .await()

            statusSuccess(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun getLanguageCollections(): Status<List<LanguageCollectionDomain>> {
        return try {
            val languageList = queryCollectionByLanguages
                .get()
                .await().map {
                    it.toObject(
                        LanguageCollectionDomain::class.java
                    )
                }

            if (languageList.isNotEmpty()) {
                statusSuccess(languageList)
            } else {
                statusEmpty()
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun getPhrasesByLanguageCollections(
        languageId: String
    ): Status<List<PhraseDomain>> {
        return try {
            val phraseList = queryCollectionByLanguages
                .document(languageId)
                .collection(COLLECTION_PHRASES)
                .whereLessThanOrEqualTo("nextReviewTimestamp", System.currentTimeMillis())
                .get()
                .await().map {
                    it.toObject(PhraseDomain::class.java)
                }

            if (phraseList.isNotEmpty()) {
                statusSuccess(phraseList)
            } else {
                statusEmpty()
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun isPhraseSaved(originalText: String): Boolean {
        val languageId = languageCollectionMapper(originalText).id
        val phraseId = originalText.encodeId()
        return try {
            queryCollectionByLanguages
                .document(languageId)
                .collection(COLLECTION_PHRASES)
                .document(phraseId)
                .get().await().exists()
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            false
        }
    }

    override suspend fun deletePhraseSaved(originalText: String) {
        val languageId = languageCollectionMapper(originalText).id
        val phraseId = originalText.encodeId()
        try {
            queryCollectionByLanguages
                .document(languageId)
                .collection(COLLECTION_PHRASES)
                .document(phraseId)
                .delete().await()
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }

    companion object {
        private const val COLLECTION_DECKS = "decks"
        private const val COLLECTION_LANGUAGES = "languages"
        private const val COLLECTION_PHRASES = "phrases"
    }
}
