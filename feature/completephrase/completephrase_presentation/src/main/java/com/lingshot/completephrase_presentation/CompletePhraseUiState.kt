package com.lingshot.completephrase_presentation

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.phrase.phrasemaster_domain.model.PhraseDomain

data class CompletePhraseUiState(
    val isSpeechActive: Boolean = true,
    val phrasesByLanguageCollectionsStatus: Status<List<PhraseDomain>> = statusDefault()
)
