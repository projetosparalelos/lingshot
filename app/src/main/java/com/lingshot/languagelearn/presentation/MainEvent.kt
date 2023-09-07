package com.lingshot.languagelearn.presentation

sealed class MainEvent {
    object ToggleServiceButton : MainEvent()

    object HideBalloonOverlay : MainEvent()
}
