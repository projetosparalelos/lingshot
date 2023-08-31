package com.lingshot.domain.usecase

import com.lingshot.domain.repository.GoogleAuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {

    suspend operator fun invoke() {
        googleAuthRepository.signOut()
    }
}
