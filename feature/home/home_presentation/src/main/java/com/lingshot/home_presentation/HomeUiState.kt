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

import com.lingshot.home_domain.model.HomeSection
import com.lingshot.home_domain.model.HomeTypeSection
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale

data class HomeUiState(
    val languageFrom: AvailableLanguage? = AvailableLanguage.ENGLISH,
    val languageTo: AvailableLanguage? = AvailableLanguage.from(Locale.getDefault().language),
    val selectedOptionsLanguage: AvailableLanguage? = null,
    val translateLanguageType: TranslateLanguageType? = null,
    val isLanguageDialogVisible: Boolean = false,
    val isServiceRunning: Boolean = false,
) {

    val homeSection = listOf(
        HomeSection(HomeTypeSection.CARD_LANGUAGE_CHOICE),
        HomeSection(HomeTypeSection.CARD_BUTTON_SCREEN_SHOT),
    ).toImmutableList()

    val availableLanguageList: ImmutableList<AvailableLanguage>
        get() = enumValues<AvailableLanguage>()
            .toList()
            .sortedBy { it }
            .map { it }
            .toImmutableList()
}
