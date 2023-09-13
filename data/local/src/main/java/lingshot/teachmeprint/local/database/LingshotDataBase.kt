package lingshot.teachmeprint.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.database.dao.UserDao
import lingshot.teachmeprint.local.model.GoalsEntity
import lingshot.teachmeprint.local.model.UserLocalEntity

@Database(
    entities = [UserLocalEntity::class, GoalsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class LingshotDataBase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val goalsDao: GoalsDao
}
