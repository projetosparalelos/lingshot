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
package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SavePhrasesCompletedGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userProfileUseCase: UserProfileUseCase,
) {

    suspend operator fun invoke() {
        val userId = userProfileUseCase()?.userId.toString()
        val date = LocalDate.now().toString()

        try {
            val getByUser = userLocalRepository.getByUser(userId).first()
            val getGoalByUserAndDate = goalsRepository.getGoalByUserAndDate(userId, date).first()

            withContext(Dispatchers.IO) {
                if (getByUser != null) {
                    val newValueGoal = getGoalByUserAndDate?.copy(targetPhrases = getByUser.goal)
                        ?: GoalsDomain(userId = userId, date = date, targetPhrases = getByUser.goal)

                    val progressPhrases = (newValueGoal.progressPhrases + 1)
                    val newValue = newValueGoal.copy(progressPhrases = progressPhrases)
                    goalsRepository.upsertGoal(newValue)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }
}
