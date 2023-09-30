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
@file:Suppress("ComplexCondition", "LongParameterList")

package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.common.util.combine
import com.lingshot.domain.usecase.RetrieveGoalsUseCase
import com.lingshot.domain.usecase.SaveGoalsUseCase
import com.lingshot.domain.usecase.SignOutUseCase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveConsecutiveDaysUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesPendingReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val saveGoalsUseCase: SaveGoalsUseCase,
    private val retrieveGoalsUseCase: RetrieveGoalsUseCase,
    retrieveLanguageCollectionsUseCase: RetrieveLanguageCollectionsUseCase,
    retrievePhrasesPendingReviewUseCase: RetrievePhrasesPendingReviewUseCase,
    retrieveConsecutiveDaysUseCase: RetrieveConsecutiveDaysUseCase,
) : ViewModel() {

    private val userDomain = flow { emit(userProfileUseCase()) }
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        combine(
            retrieveLanguageCollectionsUseCase(),
            retrievePhrasesPendingReviewUseCase(),
            retrieveConsecutiveDaysUseCase(),
            retrieveGoalsUseCase(),
            _uiState,
            userDomain,
        ) { languageCollectionsStatus,
            phrasesPendingReviewStatus,
            consecutiveDaysStatus,
            goals,
            uiState, userDomain,
            ->
            if (languageCollectionsStatus.isLoadingStatus.not() &&
                phrasesPendingReviewStatus.isLoadingStatus.not() &&
                consecutiveDaysStatus.isLoadingStatus.not() &&
                userDomain != null
            ) {
                uiState.copy(
                    userDomain = userDomain,
                    goals = goals,
                    isPieChartGoalsVisible = true,
                    phrasesPendingReviewStatus = phrasesPendingReviewStatus,
                    languageCollectionsStatus = languageCollectionsStatus,
                    consecutiveDaysStatus = consecutiveDaysStatus,
                )
            } else {
                uiState
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    fun handleEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.SaveGoals -> {
                saveGoals(homeEvent.day)
            }
            is HomeEvent.SelectedGoalDays -> {
                selectedGoalDays(homeEvent.day)
            }
            is HomeEvent.SignOut -> {
                signOut(homeEvent.block)
            }
            is HomeEvent.ToggleExpandDropdownMenuSignOut -> {
                toggleExpandDropdownMenuSignOut()
            }
            is HomeEvent.ToggleSetGoalsDialog -> {
                toggleSetGoalsDialog()
            }
        }
    }

    private fun saveGoals(day: Int) {
        viewModelScope.launch {
            saveGoalsUseCase(day)
        }.invokeOnCompletion {
            toggleSetGoalsDialog()
        }
    }

    private fun selectedGoalDays(day: Int) {
        _uiState.update { it.copy(selectedGoalDays = day) }
    }

    private fun signOut(block: () -> Unit) {
        viewModelScope.launch {
            signOutUseCase()
            delay(1.seconds)
            toggleExpandDropdownMenuSignOut()
            delay(100.milliseconds)
        }.invokeOnCompletion {
            block()
        }
    }

    private fun toggleExpandDropdownMenuSignOut() {
        _uiState.update { it.copy(isExpandedDropdownMenuSignOut = !it.isExpandedDropdownMenuSignOut) }
    }

    private fun toggleSetGoalsDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedGoalDays = retrieveGoalsUseCase().first().first?.goal ?: 1,
                    isSetGoalsDialogVisible = !it.isSetGoalsDialogVisible,
                )
            }
        }
    }
}
