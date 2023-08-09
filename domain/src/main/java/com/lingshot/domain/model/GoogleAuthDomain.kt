package com.lingshot.domain.model

data class UserDomain(
    val userId: String = "",
    val username: String? = "",
    val profilePictureUrl: String? = ""
) {
    val firstName: String? = username?.split(" ")?.get(0)
}

data class SignInResult(
    val userDomain: UserDomain?,
    val errorMessage: String?
)
