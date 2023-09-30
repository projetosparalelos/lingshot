/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lingshot.languagelearn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.repository.BalloonOverlayRepository
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.usecase.UpdateConsecutiveDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val balloonOverlayRepository: BalloonOverlayRepository,
    private val updateConsecutiveDaysUseCase: UpdateConsecutiveDaysUseCase,
    private val userProfileUseCase: UserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())

    init {
        updateConsecutiveDays()
    }

    val isSignInSuccessful: StateFlow<Boolean> =
        flow { emit(userProfileUseCase()) }
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    val uiState: StateFlow<MainUiState> =
        combine(
            balloonOverlayRepository.isBalloonOverlayVisible(),
            _uiState,
        ) { isBalloonOverlayVisible, uiState ->
            uiState.copy(
                isBalloonOverlayVisible = isBalloonOverlayVisible,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value,
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

    private fun updateConsecutiveDays() {
        viewModelScope.launch {
            updateConsecutiveDaysUseCase(isFirstTimeNotFromMain = true)
        }
    }
}
