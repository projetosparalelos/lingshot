package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RetrieveConsecutiveDaysUseCase @Inject constructor(
    private val consecutiveDaysRepository: ConsecutiveDaysRepository
) {
    operator fun invoke(): Flow<Status<Int>> = flow {
        try {
            val data = consecutiveDaysRepository.getConsecutiveDays()

            if (data != null) {
                emit(statusSuccess(data.consecutiveDays))
            } else {
                emit(statusSuccess(0))
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emit(statusError(e.message))
        }
    }
}
