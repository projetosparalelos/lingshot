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

package lingshot.teachmeprint.local.repository

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.mapper.toGoalsDomain
import lingshot.teachmeprint.local.mapper.toGoalsEntity
import javax.inject.Inject

class GoalsRepositoryImpl @Inject constructor(
    private val goalsDao: GoalsDao,
) : GoalsRepository {

    override fun upsertGoal(goals: GoalsDomain) {
        val goalsEntity = goals.toGoalsEntity()
        goalsDao.upsertGoal(goalsEntity)
    }

    override fun getGoalByUserAndDate(userId: String, date: String): Flow<GoalsDomain?> {
        return goalsDao.getGoalByUserAndDate(userId, date).map {
            it?.toGoalsDomain()
        }
    }
}
