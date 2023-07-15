package com.lingshot.domain.model

data class LanguageCodeFromAndToDomain(
    val name: String = ""
)

data class PhraseDomain(
    val original: String = "",
    val translate: String = ""
)
