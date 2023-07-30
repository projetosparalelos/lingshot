package com.phrase.phrasemaster_domain.usecase

import com.phrase.phrasemaster_domain.model.LanguageCodeFromAndToDomain
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
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
