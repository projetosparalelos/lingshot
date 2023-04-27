package com.teachmeprint.language.feature.screenshot.model.event

import android.graphics.Bitmap
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType

sealed class ScreenShotEvent {
    object ShowDialogLanguage : ScreenShotEvent()
    data class CroppedImage(
        val actionCropImageType: ActionCropImageType?
    ) : ScreenShotEvent()

    data class ShowBalloon(
        val textTranslate: String
    ) : ScreenShotEvent()

    data class OptionSelectedNavigationBar(
        val selectedOptionNavigationBar: NavigationBarItemType
    ) : ScreenShotEvent()

    data class OptionSelectedLanguage(
        val selectedOptionLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class SaveLanguage(
        val availableLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?
    ) : ScreenShotEvent()
}