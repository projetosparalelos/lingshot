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

package com.phrase.phrasemaster_domain.mapper

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.model.encodeId
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LanguageCollectionMapper @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
) {
    suspend operator fun invoke(text: String): LanguageCollectionDomain {
        val from = languageIdentifierUseCase(text)
        val to = languageChoiceRepository.getLanguage().first()?.languageCode.toString()
        return LanguageCollectionDomain(
            id = "${from}_$to".encodeId(),
            from = from,
            to = to,
        )
    }
}
