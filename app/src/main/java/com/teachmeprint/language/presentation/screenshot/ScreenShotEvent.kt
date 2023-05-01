package com.teachmeprint.language.presentation.screenshot

import android.graphics.Bitmap
import com.teachmeprint.language.data.model.language.AvailableLanguage
import com.teachmeprint.language.presentation.screenshot.ui.component.ActionCropImage
import com.teachmeprint.language.presentation.screenshot.ui.component.NavigationBarItem

sealed class ScreenShotEvent {
    object HideTranslateBalloon : ScreenShotEvent()

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

    data class ShowTranslateBalloon(
        val textTranslate: String
    ) : ScreenShotEvent()

    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?
    ) : ScreenShotEvent()
}