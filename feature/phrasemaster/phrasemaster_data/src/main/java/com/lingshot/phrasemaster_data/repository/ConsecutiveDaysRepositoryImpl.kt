package com.lingshot.phrasemaster_data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.model.ConsecutiveDaysDomain
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ConsecutiveDaysRepositoryImpl @Inject constructor(
    private val useCase: UserProfileUseCase
) : ConsecutiveDaysRepository {

    private val db = Firebase.firestore

    private val queryCollectionByConsecutiveDays
        get() =
            db.collection(COLLECTION_CONSECUTIVE_DAYS)
                .document(useCase()?.userId.toString())

    override suspend fun updateConsecutiveDays(consecutiveDaysDomain: ConsecutiveDaysDomain) {
        queryCollectionByConsecutiveDays.set(consecutiveDaysDomain).await()
    }

    override suspend fun getConsecutiveDays(): ConsecutiveDaysDomain? {
        val querySnapshot = queryCollectionByConsecutiveDays.get().await()
        return querySnapshot.toObject(ConsecutiveDaysDomain::class.java)
    }

    companion object {
        private const val COLLECTION_CONSECUTIVE_DAYS = "consecutive_days"
    }
}
