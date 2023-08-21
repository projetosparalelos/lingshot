package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.reviewlevel_domain.model.ReviewLevel
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.from
import com.lingshot.reviewlevel_domain.model.ReviewLevel.Companion.getNextReviewTimestamp
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository

class UpdatePhraseReviewUseCase(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    suspend operator fun invoke(languageId: String?, phraseDomain: PhraseDomain): Status<Unit> {
        var reviewLevel = from(phraseDomain.reviewLevel).level

        if (reviewLevel <= ReviewLevel.MASTER.level) {
            reviewLevel += 1
        }

        val nextReviewTimestamp = getNextReviewTimestamp(from(reviewLevel))

        return phraseCollectionRepository.updatePhraseInLanguageCollections(
            languageId = languageId.toString(),
            phraseDomain = phraseDomain.copy(
                reviewLevel = reviewLevel,
                nextReviewTimestamp = nextReviewTimestamp
            )
        )
    }
}
