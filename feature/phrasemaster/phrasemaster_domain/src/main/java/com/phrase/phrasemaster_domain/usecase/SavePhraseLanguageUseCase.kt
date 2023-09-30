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

package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SavePhraseLanguageUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
) {
    suspend operator fun invoke(phraseDomain: PhraseDomain): SaveOrDeleteResult {
        val availableLanguage = AvailableLanguage.from(
            languageIdentifierUseCase(phraseDomain.original),
        )

        if (availableLanguage?.languageCode == null) {
            return SaveOrDeleteResult.InvalidLanguage
        }

        val isPhraseSaved = saveOrDeletePhraseInLanguageCollection(phraseDomain)
        return SaveOrDeleteResult.Success(isPhraseSaved)
    }

    private suspend fun saveOrDeletePhraseInLanguageCollection(
        phraseDomain: PhraseDomain,
    ): Boolean {
        return try {
            if (phraseCollectionRepository.isPhraseSaved(
                    phraseDomain.original,
                )
            ) {
                phraseCollectionRepository.deletePhraseSaved(phraseDomain.original)
                false
            } else {
                phraseCollectionRepository.savePhraseInLanguageCollections(phraseDomain)
                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            false
        }
    }
}

sealed class SaveOrDeleteResult {
    object InvalidLanguage : SaveOrDeleteResult()
    data class Success(val isPhraseSaved: Boolean) : SaveOrDeleteResult()
}
