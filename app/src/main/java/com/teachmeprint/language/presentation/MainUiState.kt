package com.teachmeprint.language.presentation

import com.teachmeprint.domain.model.Status
import com.teachmeprint.domain.model.UserDomain
import com.teachmeprint.domain.model.statusDefault

data class MainUiState(
    val result: Status<Unit>? = statusDefault(),
    val userDomain: UserDomain? = null,
    val isServiceRunning: Boolean = false
)