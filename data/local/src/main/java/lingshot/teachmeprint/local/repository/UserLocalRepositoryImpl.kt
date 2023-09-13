package lingshot.teachmeprint.local.repository

import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.repository.UserLocalRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lingshot.teachmeprint.local.database.dao.UserDao
import lingshot.teachmeprint.local.mapper.toUserLocalDomain
import lingshot.teachmeprint.local.mapper.toUserLocalEntity

class UserLocalRepositoryImpl @Inject constructor(
    private val userDao: UserDao
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
