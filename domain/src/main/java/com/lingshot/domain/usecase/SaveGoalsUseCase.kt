package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber

class SaveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    suspend operator fun invoke(targetPhrases: Int) {
        val userId = userProfileUseCase()?.userId.toString()
        val date = LocalDate.now().toString()
        try {
            val goals = goalsRepository.getGoalByUserAndDate(userId, date).first()
            withContext(Dispatchers.IO) {
                if (goals != null) {
                    val newValue = goals.copy(targetPhrases = targetPhrases)
                    goalsRepository.upsertGoal(newValue)
                } else {
                    goalsRepository.upsertGoal(
                        goals = GoalsDomain(
                            userId = userId,
                            date = date,
                            targetPhrases = targetPhrases
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }
}
