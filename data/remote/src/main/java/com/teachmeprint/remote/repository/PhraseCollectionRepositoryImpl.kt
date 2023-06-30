package com.teachmeprint.remote.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teachmeprint.domain.model.LanguageDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.model.Status
import com.teachmeprint.domain.model.statusError
import com.teachmeprint.domain.model.statusSuccess
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import com.teachmeprint.domain.usecase.UserProfileUseCase
import com.teachmeprint.remote.util.encodeBase
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class PhraseCollectionRepositoryImpl @Inject constructor(
    private val useCase: UserProfileUseCase
) : PhraseCollectionRepository {

    private val db = Firebase.firestore

    private val queryCollectionByLanguages
        get() =
            db.collection(COLLECTION_DECKS)
                .document(useCase()?.userId.toString())
                .collection(COLLECTION_LANGUAGES)

    override suspend fun savePhraseInLanguageCollections(
        languageDomain: LanguageDomain,
        phraseDomain: PhraseDomain
    ): Status<Unit> {
        return try {
            queryCollectionByLanguages
                .document(languageDomain.name)
                .apply { set(languageDomain) }
                .collection(COLLECTION_PHRASES)
                .document(phraseDomain.original.encodeBase())
                .set(phraseDomain)
                .await()

            statusSuccess(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun getLanguageCollections(): Status<List<LanguageDomain>> {
        return try {
            val languageList = queryCollectionByLanguages
                .get()
                .await().map {
                    it.toObject(LanguageDomain::class.java)
                }
            statusSuccess(languageList)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun getPhrasesByLanguageCollections(
        languageDomain: LanguageDomain
    ): Status<List<PhraseDomain>> {
        return try {
            val phraseList = queryCollectionByLanguages
                .document(languageDomain.name)
                .collection(COLLECTION_PHRASES)
                .get()
                .await().map {
                    it.toObject(PhraseDomain::class.java)
                }
            statusSuccess(phraseList)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }

    override suspend fun isPhraseSaved(languageId: String, phraseId: String): Boolean {
        return try {
            queryCollectionByLanguages
                .document(languageId)
                .collection(COLLECTION_PHRASES)
                .document(phraseId.encodeBase())
                .get().await().exists()
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            false
        }
    }

    override suspend fun deletePhraseSaved(languageId: String, phraseId: String) {
        try {
            queryCollectionByLanguages
                .document(languageId)
                .collection(COLLECTION_PHRASES)
                .document(phraseId.encodeBase())
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