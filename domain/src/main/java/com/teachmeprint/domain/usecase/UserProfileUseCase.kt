package com.teachmeprint.domain.usecase

import com.teachmeprint.domain.model.UserDomain
import com.teachmeprint.domain.repository.GoogleAuthRepository
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {

    operator fun invoke(): UserDomain? = googleAuthRepository.getSignedInUser()
}