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

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.lingshot.reviewlevel_domain.model.ReviewLevel
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.from
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.getNextReviewTimestamp
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UpdatePhraseReviewUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository,
) {
    suspend operator fun invoke(languageId: String, phraseDomain: PhraseDomain): Status<Unit> {
        return try {
            var reviewLevel = from(phraseDomain.reviewLevel).level

            if (reviewLevel <= ReviewLevel.MASTER.level) {
                reviewLevel += 1
            }

            val nextReviewTimestamp = getNextReviewTimestamp(from(reviewLevel))

            phraseCollectionRepository.updatePhraseInLanguageCollections(
                languageId = languageId,
                phraseDomain = phraseDomain.copy(
                    reviewLevel = reviewLevel,
                    nextReviewTimestamp = nextReviewTimestamp,
                ),
            )
            statusSuccess(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }
}
