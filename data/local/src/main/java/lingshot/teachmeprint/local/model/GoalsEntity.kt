package lingshot.teachmeprint.local.model

import androidx.room.Entity

@Entity(
    tableName = "goals",
    primaryKeys = ["userId", "date"]
)
data class GoalsEntity(
    val userId: String,
    val date: String,
    val targetPhrases: Int,
    val progressPhrases: Int
)
