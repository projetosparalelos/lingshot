package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import com.phrase.phrasemaster_domain.usecase.RetrievePhrasesPendingReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    retrieveLanguageCollectionsUseCase: RetrieveLanguageCollectionsUseCase,
    retrievePhrasesPendingReviewUseCase: RetrievePhrasesPendingReviewUseCase
) : ViewModel() {

    private val userDomain = flow { emit(userProfileUseCase()) }
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        combine(
            retrieveLanguageCollectionsUseCase(),
            retrievePhrasesPendingReviewUseCase(),
            _uiState,
            userDomain
        ) { languageCollectionsStatus,
            phrasesPendingReviewStatus,
            uiState, userDomain ->

            if (languageCollectionsStatus.isLoadingStatus.not() &&
                phrasesPendingReviewStatus.isLoadingStatus.not() &&
                userDomain != null
            ) {
                uiState.copy(
                    userDomain = userDomain,
                    phrasesPendingReviewStatus = phrasesPendingReviewStatus,
                    languageCollectionsStatus = languageCollectionsStatus
                )
            } else {
                uiState
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = _uiState.value
        )
}
