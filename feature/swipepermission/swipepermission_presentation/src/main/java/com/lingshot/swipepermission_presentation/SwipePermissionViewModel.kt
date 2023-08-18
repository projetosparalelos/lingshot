package com.lingshot.swipepermission_presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.repository.GoogleAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SwipePermissionViewModel @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SwipePermissionUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(swipePermissionEvent: SwipePermissionEvent) {
        when (swipePermissionEvent) {
            is SwipePermissionEvent.ClearState -> {
                clearState()
            }

            is SwipePermissionEvent.SignInWithIntent -> {
                signInWithIntent(swipePermissionEvent.intent)
            }
        }
    }

    suspend fun signIn() = googleAuthRepository.signIn()

    fun fetchSignIn(
        userDomain: UserDomain? = googleAuthRepository.getSignedInUser(),
        signInError: String? = null
    ) {
        _uiState.update {
            it.copy(
                isSignInSuccessful = (userDomain != null),
                signInError = signInError
            )
        }
    }

    private fun signInWithIntent(intent: Intent?) {
        viewModelScope.launch {
            googleAuthRepository.signInWithIntent(intent = intent ?: return@launch).also {
                fetchSignIn(it.userDomain, it.errorMessage)
            }
        }
    }

    private fun clearState() {
        _uiState.update { SwipePermissionUiState() }
    }
}
