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

import com.phrase.phrasemaster_domain.model.PhraseDomain

sealed class CompletePhraseEvent {

    object ClearState : CompletePhraseEvent()

    object FetchAnswersFinished : CompletePhraseEvent()

    object FetchAnswerSound : CompletePhraseEvent()

    object HideAnswerSheet : CompletePhraseEvent()

    object ToggleTranslatedTextVisibility : CompletePhraseEvent()

    data class FillWord(val word: String) : CompletePhraseEvent()

    data class ShowAnswerSheet(val isAnswerCorrect: Boolean) : CompletePhraseEvent()

    data class FetchTextToSpeech(val text: String, val languageFrom: String) : CompletePhraseEvent()

    data class UpdatePhrasePositionOnSuccess(
        val languageId: String,
        val phraseDomain: PhraseDomain,
    ) : CompletePhraseEvent()

    data class UpdatePhrasePositionOnError(
        val phraseDomain: PhraseDomain,
    ) : CompletePhraseEvent()
}
