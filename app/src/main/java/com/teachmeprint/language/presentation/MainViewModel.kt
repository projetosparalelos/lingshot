package com.teachmeprint.language.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.ToggleServiceButton -> {
                toggleServiceButton()
            }
        }
    }

    private fun toggleServiceButton() {
        _uiState.update { it.copy(isServiceRunning = !it.isServiceRunning) }
    }
}