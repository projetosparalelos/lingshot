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
import javax.inject.Singleton
import lingshot.teachmeprint.local.database.LingshotDataBase
import lingshot.teachmeprint.local.database.dao.GoalsDao
import lingshot.teachmeprint.local.database.dao.UserDao
import lingshot.teachmeprint.local.repository.GoalsRepositoryImpl
import lingshot.teachmeprint.local.repository.UserLocalRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLingshotDataBase(
        @ApplicationContext context: Context
    ): LingshotDataBase = Room.databaseBuilder(
        context,
        LingshotDataBase::class.java,
        "lingshot-language-learn-database"
    ).build()

    @Provides
    fun provideUserDao(
        lingshotDataBase: LingshotDataBase
    ): UserDao = lingshotDataBase.userDao

    @Provides
    fun provideGoalsDao(
        lingshotDataBase: LingshotDataBase
    ): GoalsDao = lingshotDataBase.goalsDao

    @Singleton
    @Provides
    fun provideUserRepository(
        dao: UserDao
    ): UserLocalRepository = UserLocalRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideGoalsRepository(
        dao: GoalsDao
    ): GoalsRepository = GoalsRepositoryImpl(dao)
}
