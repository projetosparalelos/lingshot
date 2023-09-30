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

package com.lingshot.screenshot_presentation

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.phrasemaster_presentation.ui.PhraseState
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ScreenShotUiState(
    val screenShotStatus: Status<LanguageTranslationDomain> = statusDefault(),
    val correctedOriginalTextStatus: Status<String> = statusDefault(),
    val dictionaryUrl: String? = null,
    val phraseState: PhraseState = PhraseState(),
    val isEditFullScreenDialogVisible: Boolean = false,
    val isPhraseSaved: Boolean = false,
    val isLanguageSelectionAlertVisible: Boolean = false,
    val isLanguageDialogVisible: Boolean = false,
    val actionCropImage: ActionCropImage? = null,
    val availableLanguage: AvailableLanguage? = null,
    val navigationBarItem: NavigationBarItem = NavigationBarItem.TRANSLATE,
    val navigationBarItemList: ImmutableList<NavigationBarItem> =
        enumValues<NavigationBarItem>().toList().toImmutableList(),
) {
    val availableLanguageList: ImmutableList<AvailableLanguage>
        get() = enumValues<AvailableLanguage>()
            .toList()
            .sortedBy { it }
            .map { it }
            .toImmutableList()
}
