package com.lingshot.completephrase_presentation

sealed class CompletePhraseEvent {

    object ClearState : CompletePhraseEvent()

    object FetchAnswerSound : CompletePhraseEvent()

    object HideAnswerSheet : CompletePhraseEvent()

    object ToggleTranslatedTextVisibility : CompletePhraseEvent()

    data class FillWord(val word: String) : CompletePhraseEvent()

    data class ShowAnswerSheet(val isAnswerCorrect: Boolean) : CompletePhraseEvent()

    data class FetchTextToSpeech(val text: String) : CompletePhraseEvent()
}
