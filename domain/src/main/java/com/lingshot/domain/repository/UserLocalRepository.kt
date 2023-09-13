package com.lingshot.domain.repository

import com.lingshot.domain.model.UserLocalDomain
import kotlinx.coroutines.flow.Flow

interface UserLocalRepository {

    fun upsertUser(userLocalDomain: UserLocalDomain)

    fun getByUser(userId: String): Flow<UserLocalDomain?>
}
