package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.model.statusLoading
import com.lingshot.domain.usecase.UserProfileUseCase
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val phraseCollectionRepository: PhraseCollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchHome() {
        fetchUserProfile()
        fetchLanguageCollections()
    }

    private fun fetchUserProfile() {
        _uiState.update { it.copy(userDomain = userProfileUseCase()) }
    }

    private fun fetchLanguageCollections() {
        _uiState.update {
            it.copy(
                languageCollectionsStatus = statusLoading()
            )
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    languageCollectionsStatus = phraseCollectionRepository.getLanguageCollections()
                )
            }
        }
    }
}
