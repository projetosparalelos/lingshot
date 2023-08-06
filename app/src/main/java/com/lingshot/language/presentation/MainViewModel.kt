package com.lingshot.language.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

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
