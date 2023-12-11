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
package com.lingshot.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.home_presentation.helper.getPremiumProduct
import com.lingshot.home_presentation.helper.hasPremiumEntitlement
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.FROM
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.qonversion.android.sdk.Qonversion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        combine(
            languageChoiceRepository.getLanguage(FROM),
            languageChoiceRepository.getLanguage(TranslateLanguageType.TO),
            _uiState,
        ) { languageFrom, languageTo, uiState ->
            uiState.copy(
                hasPremiumPermission = Qonversion.shared.hasPremiumEntitlement(),
                premiumProduct = Qonversion.shared.getPremiumProduct(),
                languageFrom = languageFrom,
                languageTo = languageTo,
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    fun handleEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.SaveLanguage -> {
                saveLanguage(homeEvent.availableLanguage, homeEvent.translateLanguageType)
            }

            is HomeEvent.SelectedOptionsLanguage -> {
                selectedOptionsLanguage(homeEvent.selectedOptionsLanguage)
            }

            is HomeEvent.ToggleLanguageDialog -> {
                toggleLanguageDialog(homeEvent.translateLanguageType)
            }

            is HomeEvent.ToggleServiceButton -> {
                toggleServiceButton()
            }
        }
    }

    private fun toggleLanguageDialog(translateLanguageType: TranslateLanguageType?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLanguageDialogVisible = !it.isLanguageDialogVisible,
                    translateLanguageType = translateLanguageType,
                    selectedOptionsLanguage = translateLanguageType?.let { type ->
                        languageChoiceRepository.getLanguage(type).first()
                    },
                )
            }
        }
    }

    private fun selectedOptionsLanguage(selectedOptionsLanguage: AvailableLanguage?) {
        _uiState.update { it.copy(selectedOptionsLanguage = selectedOptionsLanguage) }
    }

    private fun saveLanguage(availableLanguage: AvailableLanguage?, translateLanguageType: TranslateLanguageType?) {
        viewModelScope.launch {
            translateLanguageType?.let {
                languageChoiceRepository.saveLanguage(availableLanguage, it)
            }
        }
    }

    private fun toggleServiceButton() {
        _uiState.update { it.copy(isServiceRunning = !it.isServiceRunning) }
    }
}
