package com.lingshot.screenshot_presentation

import android.graphics.Bitmap
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.screenshot_presentation.ui.component.ActionCropImage
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem

sealed class ScreenShotEvent {
    object ClearStatus : ScreenShotEvent()

    object ToggleLanguageDialog : ScreenShotEvent()

    object ToggleLanguageDialogAndHideSelectionAlert : ScreenShotEvent()

    data class CheckPhraseInLanguageCollection(
        val originalText: String
    ) : ScreenShotEvent()

    data class CroppedImage(
        val actionCropImage: ActionCropImage?
    ) : ScreenShotEvent()

    data class SaveLanguage(
        val availableLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class SavePhraseInLanguageCollection(
        val originalText: String,
        val translatedText: String
    ) : ScreenShotEvent()

    data class SelectedOptionsLanguage(
        val availableLanguage: AvailableLanguage?
    ) : ScreenShotEvent()

    data class SelectedOptionsNavigationBar(
        val navigationBarItem: NavigationBarItem
    ) : ScreenShotEvent()

    data class FetchCorrectedOriginalText(
        val originalText: String
    ) : ScreenShotEvent()

    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?
    ) : ScreenShotEvent()

    data class ToggleDictionaryFullScreenPopup(
        val url: String?
    ) : ScreenShotEvent()
}