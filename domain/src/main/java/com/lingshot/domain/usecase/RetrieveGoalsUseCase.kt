package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.repository.GoalsRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

class RetrieveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    operator fun invoke(): Flow<GoalsDomain?> {
        return try {
            val userId = userProfileUseCase()?.userId.toString()
            val date = LocalDate.now().toString()

            goalsRepository.getGoalByUserAndDate(userId = userId, date = date)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emptyFlow()
        }
    }
}
