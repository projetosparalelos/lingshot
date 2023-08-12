package com.lingshot.completephrase_presentation

sealed class CompletePhraseEvent {

    object ClearState : CompletePhraseEvent()

    object ToggleTranslatedTextVisibility : CompletePhraseEvent()

    data class FetchTextToSpeech(val text: String) : CompletePhraseEvent()
}
