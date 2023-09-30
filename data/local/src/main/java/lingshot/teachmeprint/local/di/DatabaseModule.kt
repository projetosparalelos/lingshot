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

package lingshot.teachmeprint.local.di

import android.content.Context
import androidx.room.Room
import com.lingshot.domain.repository.GoalsRepository
import com.lingshot.domain.repository.UserLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lingshot.teachmeprint.local.database.LingshotDataBase
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.database.dao.UserDao
import lingshot.teachmeprint.local.repository.GoalsRepositoryImpl
import lingshot.teachmeprint.local.repository.UserLocalRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLingshotDataBase(
        @ApplicationContext context: Context,
    ): LingshotDataBase = Room.databaseBuilder(
        context,
        LingshotDataBase::class.java,
        "lingshot-language-learn-database",
    ).build()

    @Provides
    fun provideUserDao(
        lingshotDataBase: LingshotDataBase,
    ): UserDao = lingshotDataBase.userDao

    @Provides
    fun provideGoalsDao(
        lingshotDataBase: LingshotDataBase,
    ): GoalsDao = lingshotDataBase.goalsDao

    @Singleton
    @Provides
    fun provideUserRepository(
        dao: UserDao,
    ): UserLocalRepository = UserLocalRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideGoalsRepository(
        dao: GoalsDao,
    ): GoalsRepository = GoalsRepositoryImpl(dao)
}
