package lingshot.teachmeprint.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserLocalEntity(
    @PrimaryKey
    val userId: String,
    val goal: Int
)
