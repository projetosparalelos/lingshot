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

import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType

sealed class HomeEvent {

    data object ToggleServiceButton : HomeEvent()

    data class SaveLanguage(val availableLanguage: AvailableLanguage?, val translateLanguageType: TranslateLanguageType?) : HomeEvent()

    data class SelectedOptionsLanguage(val selectedOptionsLanguage: AvailableLanguage?) : HomeEvent()

    data class ToggleLanguageDialog(val translateLanguageType: TranslateLanguageType?) : HomeEvent()
}
