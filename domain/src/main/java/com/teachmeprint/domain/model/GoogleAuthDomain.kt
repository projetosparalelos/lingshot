package com.teachmeprint.domain.model

data class UserDomain(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

data class SignInResult(
    val userDomain: UserDomain?,
    val errorMessage: String?
)