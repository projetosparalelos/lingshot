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

package com.lingshot.swipepermission_presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.repository.GoogleAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipePermissionViewModel @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository,
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
        signInError: String? = null,
    ) {
        _uiState.update {
            it.copy(
                isSignInSuccessful = (userDomain != null),
                signInError = signInError,
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
