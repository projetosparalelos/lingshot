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
package lingshot.teachmeprint.local.repository

import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lingshot.teachmeprint.local.database.dao.UserDao
import lingshot.teachmeprint.local.mapper.toUserLocalDomain
import lingshot.teachmeprint.local.mapper.toUserLocalEntity
import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserLocalRepository {

    override fun upsertUser(userLocalDomain: UserLocalDomain) {
        val userLocalEntity = userLocalDomain.toUserLocalEntity()
        userDao.upsertUser(userLocalEntity)
    }

    override fun getByUser(userId: String): Flow<UserLocalDomain?> {
        return userDao.getByUser(userId).map {
            it?.toUserLocalDomain()
        }
    }
}
