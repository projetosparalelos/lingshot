package com.lingshot.screenshot_presentation

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem
import com.phrase.phrasemaster_domain.model.PhraseDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ScreenShotUiState(
    val screenShotStatus: Status<LanguageTranslationDomain> = statusDefault(),
    val correctedOriginalTextStatus: Status<String> = statusDefault(),
    val dictionaryUrl: String? = null,
    val phraseDomain: PhraseDomain? = null,
    val isPhraseSaved: Boolean = false,
    val isLanguageSelectionAlertVisible: Boolean = false,
    val isLanguageDialogVisible: Boolean = false,
    val actionCropImage: ActionCropImage? = null,
    val availableLanguage: AvailableLanguage? = null,
    val navigationBarItem: NavigationBarItem = NavigationBarItem.TRANSLATE,
    val navigationBarItemList: ImmutableList<NavigationBarItem> =
        enumValues<NavigationBarItem>().toList().toImmutableList()
) {
    val availableLanguageList: ImmutableList<AvailableLanguage>
        get() = enumValues<AvailableLanguage>()
            .toList()
            .sortedBy { it }
            .map { it }
            .toImmutableList()
}
