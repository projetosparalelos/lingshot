package com.phrase.phrasemaster_domain.repository

import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain

interface PhraseCollectionRepository {

    suspend fun savePhraseInLanguageCollections(
        phraseDomain: PhraseDomain
    )

    suspend fun updatePhraseInLanguageCollections(
        languageId: String,
        phraseDomain: PhraseDomain
    )

    suspend fun getLanguageCollections(): List<LanguageCollectionDomain>

    suspend fun getPhrasesForNextReview(
        languageId: String
    ): List<PhraseDomain>

    suspend fun isPhraseSaved(originalText: String): Boolean

    suspend fun deletePhraseSaved(originalText: String)
}
