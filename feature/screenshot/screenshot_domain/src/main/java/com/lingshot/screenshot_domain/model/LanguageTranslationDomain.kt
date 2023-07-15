package com.lingshot.screenshot_domain.model

data class LanguageTranslationDomain(
    val originalText: String,
    val translatedText: String?,
    val languageCodeFrom: String,
    val languageCodeTo: String
) {
    val dictionaryUrl: (String) -> String = { word ->
        DictionaryType.REVERSO_CONTEXT.url(
            languageCodeFrom,
            languageCodeTo,
            word
        )
    }
}
