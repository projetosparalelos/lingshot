package com.lingshot.language.presentation

sealed class MainEvent {
    object ToggleServiceButton : MainEvent()

    object HideBalloonOverlay : MainEvent()
}
