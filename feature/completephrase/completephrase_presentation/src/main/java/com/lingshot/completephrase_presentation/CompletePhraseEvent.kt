package com.lingshot.completephrase_presentation

sealed class CompletePhraseEvent {

    data class FetchTextToSpeech(val text: String) : CompletePhraseEvent()
}
