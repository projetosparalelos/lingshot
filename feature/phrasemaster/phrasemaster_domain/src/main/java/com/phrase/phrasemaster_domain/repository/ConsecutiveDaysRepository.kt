package com.phrase.phrasemaster_domain.repository

import com.phrase.phrasemaster_domain.model.ConsecutiveDaysDomain

interface ConsecutiveDaysRepository {

    suspend fun updateConsecutiveDays(consecutiveDaysDomain: ConsecutiveDaysDomain)

    suspend fun getConsecutiveDays(): ConsecutiveDaysDomain?
}
