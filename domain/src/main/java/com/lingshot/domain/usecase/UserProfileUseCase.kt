package com.lingshot.domain.usecase

import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.repository.GoogleAuthRepository
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {

    operator fun invoke(): UserDomain? = googleAuthRepository.getSignedInUser()
}
