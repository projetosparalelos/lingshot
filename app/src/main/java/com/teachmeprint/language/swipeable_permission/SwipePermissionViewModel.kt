package com.teachmeprint.language.swipeable_permission

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SwipePermissionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SwipePermissionUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(swipePermissionEvent: SwipePermissionEvent) {
        when (swipePermissionEvent) {
            is SwipePermissionEvent.HasOverlayPermission -> {
                hasOverlayPermission()
            }
        }
    }

    private fun hasOverlayPermission() {
        _uiState.update { it.copy(hasOverlayPermission = it.hasOverlayPermission.not()) }
    }
}