package com.teachmeprint.language.presentation.screenshot

import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.presentation.screenshot.ui.component.ActionCropImage
import com.teachmeprint.language.presentation.screenshot.ui.component.NavigationBarItem

data class ScreenShotUiState(
    val screenShotStatus: ScreenShotStatus = ScreenShotStatus.Default,
    val textTranslate: String = "",
    val isBalloonTranslateVisible: Boolean = false,
    val isLanguageSelectionAlertVisible: Boolean = false,
    val isLanguageDialogVisible: Boolean = false,
    val actionCropImage: ActionCropImage? = null,
    val availableLanguage: AvailableLanguage? = null,
    val navigationBarItem: NavigationBarItem = NavigationBarItem.TRANSLATE,
    val navigationBarItemList: List<NavigationBarItem> = enumValues<NavigationBarItem>().toList(),
) {
    val availableLanguageList: List<AvailableLanguage>
        get() = enumValues<AvailableLanguage>().toList()
            .sortedBy { it }
            .map { it }

}

sealed class ScreenShotStatus {
    object Default : ScreenShotStatus()
    object Loading : ScreenShotStatus()
    data class Success(val text: String?): ScreenShotStatus()
    data class Error(val code: Int?) : ScreenShotStatus()
}