package com.teachmeprint.language.presentation

import com.teachmeprint.domain.model.UserDomain

data class MainUiState(
    val userDomain: UserDomain? = null,
    val isServiceRunning: Boolean = false
)