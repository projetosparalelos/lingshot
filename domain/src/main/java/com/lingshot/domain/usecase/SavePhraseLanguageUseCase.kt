package com.lingshot.domain.usecase

import com.lingshot.domain.model.LanguageCodeFromAndToDomain
import com.lingshot.domain.model.PhraseDomain
import com.lingshot.domain.repository.PhraseCollectionRepository
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
                phraseDomain.id
            )
        ) {
            phraseCollectionRepository.deletePhraseSaved(
                languageCodeFromAndToDomain.name,
                phraseDomain.id
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
