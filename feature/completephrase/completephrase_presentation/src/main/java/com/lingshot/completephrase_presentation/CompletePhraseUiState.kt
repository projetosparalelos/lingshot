package com.lingshot.completephrase_presentation

import com.lingshot.completephrase_presentation.ui.component.AnswerState
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.phrase.phrasemaster_domain.model.PhraseDomain

data class CompletePhraseUiState(
    val answerState: AnswerState = AnswerState(),
    val isAnswerSheetVisible: Boolean = false,
    val isSpeechActive: Boolean = true,
    val isLoading: Boolean = true,
    val phrasesByLanguageCollections: List<PhraseDomain> = emptyList(),
    val isTranslatedTextVisible: Boolean = false,
    val wordToFill: String = "",
    val updatePhraseInLanguageCollectionsStatus: Status<Unit> = statusDefault()
)
