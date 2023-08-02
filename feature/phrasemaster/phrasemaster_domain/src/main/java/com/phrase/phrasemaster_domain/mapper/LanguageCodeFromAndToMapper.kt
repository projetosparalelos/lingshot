package com.phrase.phrasemaster_domain.mapper

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.model.LanguageCodeFromAndToDomain
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class LanguageCodeFromAndToMapper @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase
) {
    suspend operator fun invoke(text: String): LanguageCodeFromAndToDomain {
        return LanguageCodeFromAndToDomain(
            name = languageIdentifierUseCase(text) + "_" +
                languageChoiceRepository.getLanguage().first()?.languageCode.toString()
        )
    }
}
