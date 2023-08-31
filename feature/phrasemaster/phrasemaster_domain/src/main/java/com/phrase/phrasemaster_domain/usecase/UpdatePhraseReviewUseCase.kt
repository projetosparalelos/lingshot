package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.lingshot.reviewlevel_domain.model.ReviewLevel
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.from
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.getNextReviewTimestamp
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import timber.log.Timber

class UpdatePhraseReviewUseCase @Inject constructor(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(languageId: String?, phraseDomain: PhraseDomain): Status<Unit> {
        return try {
            var reviewLevel = from(phraseDomain.reviewLevel).level

            if (reviewLevel <= ReviewLevel.MASTER.level) {
                reviewLevel += 1
            }

            val nextReviewTimestamp = getNextReviewTimestamp(from(reviewLevel))

            phraseCollectionRepository.updatePhraseInLanguageCollections(
                languageId = languageId.toString(),
                phraseDomain = phraseDomain.copy(
                    reviewLevel = reviewLevel,
                    nextReviewTimestamp = nextReviewTimestamp
                )
            )
            statusSuccess(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            statusError(e.message)
        }
    }
}
