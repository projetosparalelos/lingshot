package com.lingshot.completephrase_presentation

import com.lingshot.completephrase_presentation.ui.component.AnswerState
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusLoading
import com.phrase.phrasemaster_domain.model.PhraseDomain

data class CompletePhraseUiState(
    val answerState: AnswerState = AnswerState(),
    val isAnswerSheetVisible: Boolean = false,
    val isSpeechActive: Boolean = true,
    val isTranslatedTextVisible: Boolean = false,
    val wordToFill: String = "",
    val phrasesByLanguageCollectionsStatus: Status<List<PhraseDomain>> = statusLoading()
)
