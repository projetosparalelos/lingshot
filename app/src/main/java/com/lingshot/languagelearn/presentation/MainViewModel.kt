package com.lingshot.languagelearn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.repository.BalloonOverlayRepository
import com.lingshot.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val balloonOverlayRepository: BalloonOverlayRepository,
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())

    val isSignInSuccessful: StateFlow<Boolean> =
        flow { emit(userProfileUseCase()) }
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    val uiState: StateFlow<MainUiState> =
        combine(
            balloonOverlayRepository.isBalloonOverlayVisible(),
            _uiState
        ) { isBalloonOverlayVisible, uiState ->
            uiState.copy(
                isBalloonOverlayVisible = isBalloonOverlayVisible
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.HideBalloonOverlay -> {
                hideBalloonOverlay()
            }

            is MainEvent.ToggleServiceButton -> {
                toggleServiceButton()
            }
        }
    }

    private fun hideBalloonOverlay() {
        viewModelScope.launch {
            balloonOverlayRepository.saveAndHideBalloonOverlay()
        }
    }

    private fun toggleServiceButton() {
        _uiState.update { it.copy(isServiceRunning = !it.isServiceRunning) }
    }
}
