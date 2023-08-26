package com.phrase.phrasemaster_domain.usecase

import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import kotlin.coroutines.cancellation.CancellationException
import timber.log.Timber

class CheckSavedPhraseUseCase(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(originalText: String): Boolean {
        return try {
            phraseCollectionRepository.isPhraseSaved(originalText)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            false
        }
    }
}
