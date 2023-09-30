/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lingshot.phrasemaster_data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.model.ConsecutiveDaysDomain
import com.phrase.phrasemaster_domain.repository.ConsecutiveDaysRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ConsecutiveDaysRepositoryImpl @Inject constructor(
    private val useCase: UserProfileUseCase,
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
