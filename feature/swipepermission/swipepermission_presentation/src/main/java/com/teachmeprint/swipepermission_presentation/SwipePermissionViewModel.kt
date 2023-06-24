package com.teachmeprint.swipepermission_presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teachmeprint.domain.model.UserDomain
import com.teachmeprint.domain.repository.GoogleAuthRepository
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

    init {
        fetchSignIn(googleAuthRepository.getSignedInUser())
    }

    fun handleEvent(swipePermissionEvent: SwipePermissionEvent) {
        when (swipePermissionEvent) {
            is SwipePermissionEvent.SignInWithIntent -> {
                signInWithIntent(swipePermissionEvent.intent)
            }
        }
    }

    suspend fun signIn() = googleAuthRepository.signIn()

    private fun signInWithIntent(intent: Intent?) {
        viewModelScope.launch {
            googleAuthRepository.signInWithIntent(intent = intent ?: return@launch).also {
                fetchSignIn(it.userDomain)
            }
        }
    }

    private fun fetchSignIn(userDomain: UserDomain?) {
        _uiState.update { it.copy(isSignInSuccessful = (userDomain != null)) }
    }
}