package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.phrase.phrasemaster_domain.model.ConsecutiveDaysDomain
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RetrieveAndUpdateConsecutiveDaysUseCase(
    private val consecutiveDaysRepository: ConsecutiveDaysRepository
) {
    operator fun invoke(isFirstTimeNotFromHome: Boolean = false): Flow<Status<Int>> = flow {
        try {
            val currentDate = LocalDate.now()
            val data = consecutiveDaysRepository.getConsecutiveDays()

            if (data != null) {
                val lastDate = LocalDate.parse(data.lastDate)
                val consecutiveDays = data.consecutiveDays

                if (lastDate != currentDate) {
                    val newData = if (lastDate == currentDate.minusDays(1) || consecutiveDays == 0) {
                        data.copy(
                            lastDate = currentDate.toString(),
                            consecutiveDays = consecutiveDays + 1
                        )
                    } else {
                        data.copy(consecutiveDays = 0)
                    }

                    consecutiveDaysRepository.updateConsecutiveDays(newData)
                }
                emit(statusSuccess(data.consecutiveDays))
            } else {
                if (isFirstTimeNotFromHome.not()) {
                    val initialData = ConsecutiveDaysDomain(
                        lastDate = currentDate.toString(),
                        consecutiveDays = 1
                    )
                    consecutiveDaysRepository.updateConsecutiveDays(initialData)
                }
                emit(statusSuccess(0))
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emit(statusError(e.message))
        }
    }
}
