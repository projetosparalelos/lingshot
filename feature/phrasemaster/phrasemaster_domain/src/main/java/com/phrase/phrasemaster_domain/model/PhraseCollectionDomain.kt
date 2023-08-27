package com.phrase.phrasemaster_domain.model

data class LanguageCollectionDomain(
    val id: String = "",
    val from: String = "",
    val to: String = ""
)

data class CollectionInfoDomain(
    val listTotalPhrases: List<Int> = emptyList(),
    val listPhrasesPlayed: List<Int> = emptyList()
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = "",
    val reviewLevel: Int = 0,
    val nextReviewTimestamp: Long = System.currentTimeMillis()
)
