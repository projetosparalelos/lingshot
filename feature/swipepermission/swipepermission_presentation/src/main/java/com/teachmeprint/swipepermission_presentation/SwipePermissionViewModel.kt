package com.teachmeprint.swipepermission_presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SwipePermissionViewModel : ViewModel() {

    val uiState: StateFlow<SwipePermissionUiState> =
        MutableStateFlow(SwipePermissionUiState())
}