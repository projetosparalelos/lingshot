package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import javax.inject.Inject

class SavePhraseLanguageUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase
) {
    suspend operator fun invoke(phraseDomain: PhraseDomain): SaveOrDeleteResult {
        val availableLanguage = AvailableLanguage.from(
            languageIdentifierUseCase(phraseDomain.original)
        )

        if (availableLanguage?.languageCode == null) {
            return SaveOrDeleteResult.InvalidLanguage
        }

        val isPhraseSaved = saveOrDeletePhraseInLanguageCollection(phraseDomain)
        return SaveOrDeleteResult.Success(isPhraseSaved)
    }

    private suspend fun saveOrDeletePhraseInLanguageCollection(
        phraseDomain: PhraseDomain
    ): Boolean {
        return if (phraseCollectionRepository.isPhraseSaved(
                phraseDomain.original
            )
        ) {
            phraseCollectionRepository.deletePhraseSaved(phraseDomain.original)
            false
        } else {
            phraseCollectionRepository.savePhraseInLanguageCollections(phraseDomain)
            true
        }
    }
}

sealed class SaveOrDeleteResult {
    object InvalidLanguage : SaveOrDeleteResult()
    data class Success(val isPhraseSaved: Boolean) : SaveOrDeleteResult()
}
