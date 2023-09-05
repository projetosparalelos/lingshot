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
import com.phrase.phrasemaster_domain.usecase.UpdateConsecutiveDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val saveGoalsUseCase: SaveGoalsUseCase,
    private val retrieveGoalsUseCase: RetrieveGoalsUseCase,
    private val updateConsecutiveDaysUseCase: UpdateConsecutiveDaysUseCase,
    retrieveLanguageCollectionsUseCase: RetrieveLanguageCollectionsUseCase,
    retrievePhrasesPendingReviewUseCase: RetrievePhrasesPendingReviewUseCase,
    retrieveConsecutiveDaysUseCase: RetrieveConsecutiveDaysUseCase
) : ViewModel() {

    private val userDomain = flow { emit(userProfileUseCase()) }
    private val _uiState = MutableStateFlow(HomeUiState())

    init {
        updateConsecutiveDays()
    }

    val uiState: StateFlow<HomeUiState> =
        combine(
            retrieveLanguageCollectionsUseCase(),
            retrievePhrasesPendingReviewUseCase(),
            retrieveConsecutiveDaysUseCase(),
            retrieveGoalsUseCase(),
            _uiState,
            userDomain
        ) { languageCollectionsStatus,
            phrasesPendingReviewStatus,
            consecutiveDaysStatus,
            goalsDomain,
            uiState, userDomain ->
            if (languageCollectionsStatus.isLoadingStatus.not() &&
                phrasesPendingReviewStatus.isLoadingStatus.not() &&
                consecutiveDaysStatus.isLoadingStatus.not() &&
                userDomain != null
            ) {
                uiState.copy(
                    userDomain = userDomain,
                    goalsDomain = goalsDomain,
                    isPieChartGoalsVisible = true,
                    phrasesPendingReviewStatus = phrasesPendingReviewStatus,
                    languageCollectionsStatus = languageCollectionsStatus,
                    consecutiveDaysStatus = consecutiveDaysStatus
                )
            } else {
                uiState
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = _uiState.value
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
                    selectedGoalDays = retrieveGoalsUseCase().first()?.targetPhrases ?: 1,
                    isSetGoalsDialogVisible = !it.isSetGoalsDialogVisible
                )
            }
        }
    }

    private fun updateConsecutiveDays() {
        viewModelScope.launch {
            updateConsecutiveDaysUseCase(isFirstTimeNotFromHome = true)
        }
    }
}
