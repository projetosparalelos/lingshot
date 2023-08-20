package com.lingshot.completephrase_presentation

import com.phrase.phrasemaster_domain.model.PhraseDomain

sealed class CompletePhraseEvent {

    object ClearState : CompletePhraseEvent()

    object FetchAnswersFinished : CompletePhraseEvent()

    object FetchAnswerSound : CompletePhraseEvent()

    object HideAnswerSheet : CompletePhraseEvent()

    object ToggleTranslatedTextVisibility : CompletePhraseEvent()

    data class FillWord(val word: String) : CompletePhraseEvent()

    data class ShowAnswerSheet(val isAnswerCorrect: Boolean) : CompletePhraseEvent()

    data class FetchTextToSpeech(val text: String) : CompletePhraseEvent()

    data class UpdatePhrasePositionOnSuccess(
        val languageId: String?,
        val phraseDomain: PhraseDomain
    ) : CompletePhraseEvent()

    data class UpdatePhrasePositionOnError(
        val phraseDomain: PhraseDomain
    ) : CompletePhraseEvent()
}
