package com.teachmeprint.domain.repository

import com.teachmeprint.domain.model.LanguageDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.model.Status

interface PhraseCollectionRepository {

    suspend fun savePhraseInLanguageCollections(
        languageDomain: LanguageDomain,
        phraseDomain: PhraseDomain
    ): Status<Unit>

    suspend fun getLanguageCollections(): Status<List<LanguageDomain>>

    suspend fun getPhrasesByLanguageCollections(
        languageDomain: LanguageDomain
    ): Status<List<PhraseDomain>>

    suspend fun isPhraseSaved(languageId: String, phraseId: String): Boolean

    suspend fun deletePhraseSaved(languageId: String, phraseId: String)
}