package com.phrase.phrasemaster_domain.model

data class LanguageCollectionDomain(
    val id: String = "",
    val from: String = "",
    val to: String = ""
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = "",
    val reviewLevel: Int = 2,
    val nextReviewTimestamp: Long = 1693081198429
)
