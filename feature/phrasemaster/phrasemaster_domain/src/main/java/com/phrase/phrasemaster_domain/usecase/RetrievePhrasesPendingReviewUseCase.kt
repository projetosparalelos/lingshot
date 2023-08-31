package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RetrievePhrasesPendingReviewUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    operator fun invoke(): Flow<Status<String>> = flow {
        try {
            emit(statusSuccess(phraseCollectionRepository.getPhrasesPendingReview()))
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emit(statusError(e.message))
        }
    }
}
