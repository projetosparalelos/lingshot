package com.phrase.phrasemaster_domain.model

data class LanguageCodeFromAndToDomain(
    val id: String = "",
    val from: String = "",
    val to: String = ""
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = "",
    val date: String = ""
)
