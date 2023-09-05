package lingshot.teachmeprint.local.mapper

import com.lingshot.domain.model.GoalsDomain
import lingshot.teachmeprint.local.model.GoalsEntity

fun GoalsEntity.toGoalsDomain() = GoalsDomain(
    userId = userId,
    date = date,
    targetPhrases = targetPhrases,
    progressPhrases = progressPhrases
)

fun GoalsDomain.toGoalsEntity() = GoalsEntity(
    userId = userId,
    date = date,
    targetPhrases = targetPhrases,
    progressPhrases = progressPhrases
)
