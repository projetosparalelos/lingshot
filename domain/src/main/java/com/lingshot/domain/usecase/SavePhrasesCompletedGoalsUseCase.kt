package com.lingshot.domain.usecase

import com.lingshot.domain.repository.GoalsRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.first
import timber.log.Timber

class SavePhrasesCompletedGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    suspend operator fun invoke() {
        val userId = userProfileUseCase()?.userId.toString()
        val date = LocalDate.now().toString()

        try {
            val goals = goalsRepository.getGoalByUserAndDate(userId, date).first()
            if (goals != null) {
                val progressPhrases = (goals.progressPhrases + 1)
                val newValue = goals.copy(progressPhrases = progressPhrases)
                goalsRepository.upsertGoal(newValue)
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
        }
    }
}
