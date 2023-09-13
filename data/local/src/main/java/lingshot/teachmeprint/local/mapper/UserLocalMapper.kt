package lingshot.teachmeprint.local.mapper

import com.lingshot.domain.model.UserLocalDomain
import lingshot.teachmeprint.local.model.UserLocalEntity

fun UserLocalEntity.toUserLocalDomain() = UserLocalDomain(
    userId = userId,
    goal = goal
)

fun UserLocalDomain.toUserLocalEntity() = UserLocalEntity(
    userId = userId,
    goal = goal
)
