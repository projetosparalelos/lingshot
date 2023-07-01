package com.teachmeprint.screenshot_domain.model

data class LanguageTranslationDomain(
    val originalText: String,
    val translatedText: String?,
    val languageCodeFromAndTo: String
)
