@file:Suppress("ComplexCondition")

package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.domain.usecase.SignOutUseCase
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveAndUpdateConsecutiveDaysUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesPendingReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    retrieveLanguageCollectionsUseCase: RetrieveLanguageCollectionsUseCase,
    retrievePhrasesPendingReviewUseCase: RetrievePhrasesPendingReviewUseCase,
    retrieveAndUpdateConsecutiveDaysUseCase: RetrieveAndUpdateConsecutiveDaysUseCase
) : ViewModel() {

    private val userDomain = flow { emit(userProfileUseCase()) }
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        combine(
            retrieveLanguageCollectionsUseCase(),
            retrievePhrasesPendingReviewUseCase(),
            retrieveAndUpdateConsecutiveDaysUseCase(isFirstTimeNotFromHome = true),
            _uiState,
            userDomain
        ) { languageCollectionsStatus,
            phrasesPendingReviewStatus,
            consecutiveDaysStatus,
            uiState, userDomain ->
            if (languageCollectionsStatus.isLoadingStatus.not() &&
                phrasesPendingReviewStatus.isLoadingStatus.not() &&
                consecutiveDaysStatus.isLoadingStatus.not() &&
                userDomain != null
            ) {
                uiState.copy(
                    userDomain = userDomain,
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
            is HomeEvent.SignOut -> {
                signOut(homeEvent.block)
            }
            is HomeEvent.ToggleExpandDropdownMenuSignOut -> {
                toggleExpandDropdownMenuSignOut()
            }
        }
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
}
