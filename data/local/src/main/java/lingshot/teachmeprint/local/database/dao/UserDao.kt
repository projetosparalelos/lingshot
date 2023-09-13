package lingshot.teachmeprint.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import lingshot.teachmeprint.local.model.UserLocalEntity

@Dao
interface UserDao {
    @Upsert
    fun upsertUser(userLocalEntity: UserLocalEntity)

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getByUser(userId: String): Flow<UserLocalEntity?>
}
