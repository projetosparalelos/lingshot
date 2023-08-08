package com.phrase.phrasemaster_domain.mapper

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.encodeId
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class LanguageCollectionMapper @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase
) {
    suspend operator fun invoke(text: String): LanguageCollectionDomain {
        val from = languageIdentifierUseCase(text)
        val to = languageChoiceRepository.getLanguage().first()?.languageCode.toString()
        return LanguageCollectionDomain(
            id = "${from}_$to".encodeId(),
            from = from,
            to = to
        )
    }
}
