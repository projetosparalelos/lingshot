package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber

class SaveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    suspend operator fun invoke(goal: Int) {
        val userId = userProfileUseCase()?.userId.toString()
        val date = LocalDate.now().toString()

        val userLocal = userLocalRepository.getByUser(userId).first()
        val goals = goalsRepository.getGoalByUserAndDate(userId, date).first()

        val newValueUserLocal = userLocal?.copy(goal = goal)
            ?: UserLocalDomain(userId = userId, goal = goal)

        val newValueGoal = goals?.copy(targetPhrases = goal)
            ?: GoalsDomain(userId = userId, date = date, targetPhrases = goal)

        try {
            withContext(Dispatchers.IO) {
                val userLocalDeferred = async { userLocalRepository.upsertUser(newValueUserLocal) }
                val goalsDeferred = async { goalsRepository.upsertGoal(newValueGoal) }

                awaitAll(userLocalDeferred, goalsDeferred)
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }
}
