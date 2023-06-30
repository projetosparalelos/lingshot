package com.teachmeprint.domain.usecase

import com.teachmeprint.domain.model.LanguageDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.model.Status
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import javax.inject.Inject

class SavePhraseLanguageUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(
        phraseDomain: PhraseDomain,
        languageDomain: LanguageDomain
    ): Status<Unit> =
        phraseCollectionRepository.savePhraseInLanguageCollections(phraseDomain, languageDomain)
}