package com.teachmeprint.language.feature.screenshot.model.state

import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType

data class ScreenShotUiState(
    val screenShotStatus: ScreenShotStatus = ScreenShotStatus.Default,
    val typeIndicatorEnum: TypeIndicatorEnum = TypeIndicatorEnum.TRANSLATE,
    val textTranslate: String = "",
    val showBalloon: Boolean = false,
    val croppedImage: Boolean = false,
    val navigationBarItemsType: List<NavigationBarItemType> = enumValues<NavigationBarItemType>().toList()
)

sealed class ScreenShotStatus {
    object Default : ScreenShotStatus()
    object Loading : ScreenShotStatus()
    data class Success(val text: String?): ScreenShotStatus()
    data class Error(val statusCode: Int?) : ScreenShotStatus()
}