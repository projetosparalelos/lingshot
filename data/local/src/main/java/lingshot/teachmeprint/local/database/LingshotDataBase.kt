package lingshot.teachmeprint.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.model.GoalsEntity

@Database(
    entities = [GoalsEntity::class],
    version = 1
)
abstract class LingshotDataBase : RoomDatabase() {

    abstract val goalsDao: GoalsDao
}
