package com.phrase.phrasemaster_domain.repository

import com.lingshot.domain.model.Status
import com.phrase.phrasemaster_domain.model.LanguageCodeFromAndToDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain

interface PhraseCollectionRepository {

    suspend fun savePhraseInLanguageCollections(
        languageCodeFromAndToDomain: LanguageCodeFromAndToDomain,
        phraseDomain: PhraseDomain
    ): Status<Unit>

    suspend fun getLanguageCollections(): Status<List<LanguageCodeFromAndToDomain>>

    suspend fun getPhrasesByLanguageCollections(
        languageCodeFromAndToDomain: LanguageCodeFromAndToDomain
    ): Status<List<PhraseDomain>>

    suspend fun isPhraseSaved(languageId: String, phraseId: String): Boolean

    suspend fun deletePhraseSaved(languageId: String, phraseId: String)
}
