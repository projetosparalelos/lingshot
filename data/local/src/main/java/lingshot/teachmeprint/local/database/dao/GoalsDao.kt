package lingshot.teachmeprint.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import lingshot.teachmeprint.local.model.GoalsEntity

@Dao
interface GoalsDao {
    @Upsert
    fun upsertGoal(goals: GoalsEntity)

    @Query("SELECT * FROM goals WHERE userId = :userId AND date = :date")
    fun getGoalByUserAndDate(userId: String, date: String): Flow<GoalsEntity?>
}
