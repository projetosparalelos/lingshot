package com.lingshot.domain.repository

import com.lingshot.domain.model.GoalsDomain
import kotlinx.coroutines.flow.Flow

interface GoalsRepository {

    suspend fun upsertGoal(goals: GoalsDomain)

    fun getGoalByUserAndDate(userId: String, date: String): Flow<GoalsDomain?>
}
