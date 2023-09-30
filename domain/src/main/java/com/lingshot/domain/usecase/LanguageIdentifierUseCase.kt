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
package com.lingshot.domain.usecase

import com.lingshot.domain.helper.FormatPhraseHelper.removeDoubleParenthesesAllPhrase
import com.lingshot.domain.model.Status
import com.lingshot.domain.repository.TextIdentifierRepository
import javax.inject.Inject

class LanguageIdentifierUseCase @Inject constructor(
    private val textIdentifierRepository: TextIdentifierRepository,
) {
    suspend operator fun invoke(text: String): String {
        val textWithoutParentheses = removeDoubleParenthesesAllPhrase(text)
        val status = textIdentifierRepository.fetchLanguageIdentifier(textWithoutParentheses)
        if (status is Status.Success) {
            return status.data.orEmpty()
        }
        return ""
    }
}
