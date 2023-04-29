package com.teachmeprint.language.feature.screenshot.model.state

import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType

data class ScreenShotUiState(
    val screenShotStatus: ScreenShotStatus = ScreenShotStatus.Default,
    val textTranslate: String = "",
    val showBalloon: Boolean = false,
    val showDialogLanguage: Boolean = false,
    val actionCropImageType: ActionCropImageType? = null,
    val selectedOptionNavigationBar: NavigationBarItemType = NavigationBarItemType.TRANSLATE,
    val navigationBarItemsType: List<NavigationBarItemType> = enumValues<NavigationBarItemType>().toList(),
    val selectedOptionLanguage: AvailableLanguage? = null
) {
    val availableLanguages: List<AvailableLanguage>
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