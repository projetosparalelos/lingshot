package com.teachmeprint.domain.usecase

import com.teachmeprint.domain.model.LanguageCodeFromAndToDomain
import com.teachmeprint.domain.model.PhraseDomain
import com.teachmeprint.domain.repository.PhraseCollectionRepository
import javax.inject.Inject

class SavePhraseLanguageUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(
        languageCodeFromAndToDomain: LanguageCodeFromAndToDomain,
        phraseDomain: PhraseDomain
    ): Boolean {
        return if (phraseCollectionRepository.isPhraseSaved(
                languageCodeFromAndToDomain.name,
                phraseDomain.original
            )
        ) {
            phraseCollectionRepository.deletePhraseSaved(
                languageCodeFromAndToDomain.name,
                phraseDomain.original
            )
            false
        } else {
            phraseCollectionRepository.savePhraseInLanguageCollections(
                languageCodeFromAndToDomain,
                phraseDomain
            )
            true
        }
    }
}