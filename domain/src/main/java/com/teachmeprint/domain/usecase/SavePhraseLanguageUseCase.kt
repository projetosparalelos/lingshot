package com.teachmeprint.domain.usecase

import com.teachmeprint.domain.model.LanguageDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import javax.inject.Inject

class SavePhraseLanguageUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(
        languageDomain: LanguageDomain,
        phraseDomain: PhraseDomain
    ): Boolean {
        return if (phraseCollectionRepository.isPhraseSaved(
                languageDomain.name,
                phraseDomain.original
            )
        ) {
            phraseCollectionRepository.deletePhraseSaved(
                languageDomain.name,
                phraseDomain.original
            )
            false
        } else {
            phraseCollectionRepository.savePhraseInLanguageCollections(
                languageDomain,
                phraseDomain
            )
            true
        }
    }
}