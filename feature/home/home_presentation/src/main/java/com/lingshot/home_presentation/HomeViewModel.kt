package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import com.lingshot.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        _uiState.update { it.copy(userDomain = userProfileUseCase()) }
    }
}
