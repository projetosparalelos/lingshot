package com.lingshot.language.presentation

import com.lingshot.domain.model.UserDomain

data class MainUiState(
    val userDomain: UserDomain? = null,
    val isServiceRunning: Boolean = false
)
