package com.lingshot.domain.usecase

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

class RetrieveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userProfileUseCase: UserProfileUseCase
) {

    operator fun invoke(): Flow<Pair<UserLocalDomain?, GoalsDomain?>> {
        return try {
            val userId = userProfileUseCase()?.userId.toString()
            val date = LocalDate.now().toString()

            val getByUser = userLocalRepository.getByUser(userId)
            val getGoalByUserAndDate = goalsRepository.getGoalByUserAndDate(
                userId = userId,
                date = date
            )

            combine(getByUser, getGoalByUserAndDate) { userLocal, goals ->
                userLocal to goals
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emptyFlow()
        }
    }
}
