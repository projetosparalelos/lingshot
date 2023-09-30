/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phrase.phrasemaster_domain.usecase

import com.phrase.phrasemaster_domain.model.ConsecutiveDaysDomain
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UpdateConsecutiveDaysUseCase @Inject constructor(
    private val consecutiveDaysRepository: ConsecutiveDaysRepository,
) {
    suspend operator fun invoke(isFirstTimeNotFromMain: Boolean = false) {
        try {
            val currentDate = LocalDate.now()
            val data = consecutiveDaysRepository.getConsecutiveDays()

            if (data != null) {
                handleExistingData(data, currentDate, isFirstTimeNotFromMain)
            } else {
                handleNoData(currentDate, isFirstTimeNotFromMain)
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }

    private suspend fun handleExistingData(
        data: ConsecutiveDaysDomain,
        currentDate: LocalDate,
        isFirstTimeNotFromMain: Boolean,
    ) {
        val lastDate = LocalDate.parse(data.lastDate)
        val consecutiveDays = data.consecutiveDays

        if (lastDate != currentDate) {
            val newData = if (lastDate == currentDate.minusDays(1) || consecutiveDays == 0) {
                if (isFirstTimeNotFromMain.not()) {
                    data.copy(
                        lastDate = currentDate.toString(),
                        consecutiveDays = consecutiveDays + 1,
                    )
                } else {
                    null
                }
            } else {
                data.copy(consecutiveDays = 0)
            }

            newData?.let { consecutiveDaysRepository.updateConsecutiveDays(it) }
        }
    }

    private suspend fun handleNoData(currentDate: LocalDate, isFirstTimeNotFromHome: Boolean) {
        if (isFirstTimeNotFromHome.not()) {
            val initialData = ConsecutiveDaysDomain(
                lastDate = currentDate.toString(),
                consecutiveDays = 1,
            )
            consecutiveDaysRepository.updateConsecutiveDays(initialData)
        }
    }
}
