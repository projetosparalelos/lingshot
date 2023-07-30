package com.phrase.phrasemaster_domain.model

data class LanguageCodeFromAndToDomain(
    val name: String = ""
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = ""
)
