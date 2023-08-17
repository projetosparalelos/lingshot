package com.phrase.phrasemaster_domain.model

import com.lingshot.reviewlevel_domain.model.ReviewLevel

data class LanguageCollectionDomain(
    val id: String = "",
    val from: String = "",
    val to: String = ""
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = "",
    val reviewLevel: Int = 0,
    val nextReviewTimestamp: Long = ReviewLevel.getNextReviewTimestamp()
)
