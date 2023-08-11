package com.phrase.phrasemaster_domain.repository

import com.lingshot.domain.model.Status
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain

interface PhraseCollectionRepository {

    suspend fun savePhraseInLanguageCollections(
        phraseDomain: PhraseDomain
    ): Status<Unit>

    suspend fun getLanguageCollections(): Status<List<LanguageCollectionDomain>>

    suspend fun getPhrasesByLanguageCollections(
        languageId: String
    ): Status<List<PhraseDomain>>

    suspend fun isPhraseSaved(originalText: String): Boolean

    suspend fun deletePhraseSaved(originalText: String)
}
