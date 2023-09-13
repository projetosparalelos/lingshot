package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber

class SavePhrasesCompletedGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    suspend operator fun invoke() {
        val userId = userProfileUseCase()?.userId.toString()
        val date = LocalDate.now().toString()

        try {
            val getByUser = userLocalRepository.getByUser(userId).first()
            val getGoalByUserAndDate = goalsRepository.getGoalByUserAndDate(userId, date).first()

            withContext(Dispatchers.IO) {
                if (getByUser != null) {
                    val newValueGoal = getGoalByUserAndDate?.copy(targetPhrases = getByUser.goal)
                        ?: GoalsDomain(userId = userId, date = date, targetPhrases = getByUser.goal)

                    val progressPhrases = (newValueGoal.progressPhrases + 1)
                    val newValue = newValueGoal.copy(progressPhrases = progressPhrases)
                    goalsRepository.upsertGoal(newValue)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }
}
