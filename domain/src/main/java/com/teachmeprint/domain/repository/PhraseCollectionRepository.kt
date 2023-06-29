package com.teachmeprint.domain.repository

import com.teachmeprint.domain.model.LanguageDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.model.Status

interface PhraseCollectionRepository {

    suspend fun savePhraseInLanguageCollections(
        phrase: PhraseDomain,
        languageDomain: LanguageDomain
    ): Status<Unit>

    suspend fun getLanguageCollections(): Status<List<LanguageDomain>>

    suspend fun getPhrasesByLanguageCollections(
        languageDomain: LanguageDomain
    ): Status<List<PhraseDomain>>
}