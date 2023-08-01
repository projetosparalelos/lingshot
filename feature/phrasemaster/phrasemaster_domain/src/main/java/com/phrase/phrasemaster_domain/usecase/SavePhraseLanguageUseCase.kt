package com.phrase.phrasemaster_domain.usecase

import android.os.Build
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
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
            phraseCollectionRepository.savePhraseInLanguageCollections(
                phraseDomain.copy(date = formatDate())
            )
            true
        }
    }

    private fun formatDate(): String {
        val pattern = "dd/MM/yyyy HH:mm:ss"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateFormat = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            LocalDateTime.now().format(dateFormat)
        } else {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            dateFormat.format(Date())
        }
    }
}

sealed class SaveOrDeleteResult {
    object InvalidLanguage : SaveOrDeleteResult()
    data class Success(val isPhraseSaved: Boolean) : SaveOrDeleteResult()
}
