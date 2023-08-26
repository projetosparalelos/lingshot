package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import kotlin.coroutines.cancellation.CancellationException
import timber.log.Timber

class RetrievePhrasesForNextReviewUseCase(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(languageId: String): Status<List<PhraseDomain>> {
        return try {
            val phraseList = phraseCollectionRepository.getPhrasesForNextReview(languageId)

            if (phraseList.isNotEmpty()) {
                statusSuccess(phraseList)
            } else {
                statusEmpty()
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }
}
