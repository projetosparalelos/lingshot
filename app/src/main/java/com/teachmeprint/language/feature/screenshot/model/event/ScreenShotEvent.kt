package com.teachmeprint.language.feature.screenshot.model.event

import android.graphics.Bitmap
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.feature.screenshot.model.ActionCropImage
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItem

sealed class ScreenShotEvent {
    object ToggleLanguageDialog : ScreenShotEvent()

    object ToggleLanguageDialogAndHideSelectionAlert : ScreenShotEvent()

    data class CroppedImage(
        val actionCropImage: ActionCropImage?
    ) : ScreenShotEvent()

    data class SaveLanguage(
        val availableLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class SelectedOptionsLanguage(
        val availableLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class SelectedOptionsNavigationBar(
        val navigationBarItem: NavigationBarItem
    ) : ScreenShotEvent()

    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?
    ) : ScreenShotEvent()

    data class ToggleTranslateBalloon(
        val textTranslate: String
    ) : ScreenShotEvent()
}