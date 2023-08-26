package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.statusLoading
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.usecase.RetrieveLanguageCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    retrieveLanguageCollectionsUseCase: RetrieveLanguageCollectionsUseCase
) : ViewModel() {

    private val languageCollectionsStatus: StateFlow<Status<List<LanguageCollectionDomain>>> =
        retrieveLanguageCollectionsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = statusLoading()
            )

    private val userDomain: StateFlow<UserDomain?> = flow {
        emit(userProfileUseCase())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = null
    )

    val uiState: StateFlow<HomeUiState> =
        combine(
            languageCollectionsStatus,
            userDomain
        ) { languageCollectionsStatus, userDomain ->
            HomeUiState(
                userDomain = userDomain,
                languageCollectionsStatus = languageCollectionsStatus
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}
