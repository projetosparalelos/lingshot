/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lingshot.completephrase_presentation

import com.lingshot.completephrase_presentation.ui.component.AnswerState
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.phrase.phrasemaster_domain.model.PhraseDomain

data class CompletePhraseUiState(
    val answerState: AnswerState = AnswerState(),
    val isAnswerSheetVisible: Boolean = false,
    val isAnswersFinished: Boolean = false,
    val isFirstTimeSavingConsecutiveDays: Boolean = true,
    val isSpeechActive: Boolean = true,
    val isLoading: Boolean = true,
    val phrasesByLanguageCollections: List<PhraseDomain> = emptyList(),
    val isTranslatedTextVisible: Boolean = false,
    val wordToFill: String = "",
    val updatePhraseInLanguageCollectionsStatus: Status<Unit> = statusDefault(),
)
