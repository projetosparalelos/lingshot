package com.lingshot.language.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val userDomain = flow { emit(userProfileUseCase()) }
    private val _uiState = MutableStateFlow(MainUiState())

    val uiState: StateFlow<MainUiState> =
        combine(
            _uiState,
            userDomain
        ) { uiState, userDomain ->
            uiState.copy(isSignInSuccessful = (userDomain != null))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

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
