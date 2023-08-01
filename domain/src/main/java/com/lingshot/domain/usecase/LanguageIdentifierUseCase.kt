package com.lingshot.domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.repository.TextIdentifierRepository
import javax.inject.Inject

class LanguageIdentifierUseCase @Inject constructor(
    private val textIdentifierRepository: TextIdentifierRepository
) {
    suspend operator fun invoke(text: String): String {
        val textWithoutBrackets = text.replace(Regex("[\\[\\]]"), "")
        val status = textIdentifierRepository.fetchLanguageIdentifier(textWithoutBrackets)
        if (status is Status.Success) {
            return status.data.orEmpty()
        }
        return ""
    }

    companion object {
        const val LANGUAGE_CODE_UNAVAILABLE = "und"
    }
}
