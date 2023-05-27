package com.teachmeprint.screenshot_presentation

import com.teachmeprint.languagechoice_domain.model.AvailableLanguage
import com.teachmeprint.screenshot_presentation.ui.component.ActionCropImage
import com.teachmeprint.screenshot_presentation.ui.component.NavigationBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ScreenShotUiState(
    val screenShotStatus: ScreenShotStatus = ScreenShotStatus.Default,
    val textTranslate: String = "",
    val isBalloonTranslateVisible: Boolean = false,
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

sealed class ScreenShotStatus {
    object Default : ScreenShotStatus()
    object Loading : ScreenShotStatus()
    data class Success(val text: String?) : ScreenShotStatus()
    data class Error(val code: Int?) : ScreenShotStatus()
}