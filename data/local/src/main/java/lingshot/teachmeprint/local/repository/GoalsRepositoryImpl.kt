package lingshot.teachmeprint.local.repository

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.mapper.toGoalsDomain
import lingshot.teachmeprint.local.mapper.toGoalsEntity

class GoalsRepositoryImpl @Inject constructor(
    private val goalsDao: GoalsDao
) : GoalsRepository {

    override suspend fun upsertGoal(goals: GoalsDomain) {
        val goalsEntity = goals.toGoalsEntity()
        goalsDao.upsertGoal(goalsEntity)
    }

    override fun getGoalByUserAndDate(userId: String, date: String): Flow<GoalsDomain?> {
        return goalsDao.getGoalByUserAndDate(userId, date).map {
            it?.toGoalsDomain()
        }
    }
}
