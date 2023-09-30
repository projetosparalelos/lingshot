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
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RetrieveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userProfileUseCase: UserProfileUseCase,
) {

    operator fun invoke(): Flow<Pair<UserLocalDomain?, GoalsDomain?>> {
        return try {
            val userId = userProfileUseCase()?.userId.toString()
            val date = LocalDate.now().toString()

            val getByUser = userLocalRepository.getByUser(userId)
            val getGoalByUserAndDate = goalsRepository.getGoalByUserAndDate(
                userId = userId,
                date = date,
            )

            combine(getByUser, getGoalByUserAndDate) { userLocal, goals ->
                userLocal to goals
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emptyFlow()
        }
    }
}
