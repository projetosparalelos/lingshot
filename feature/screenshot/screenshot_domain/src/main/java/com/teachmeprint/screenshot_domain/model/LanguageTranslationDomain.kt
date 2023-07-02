package com.teachmeprint.screenshot_domain.model

data class LanguageTranslationDomain(
    val originalText: String,
    val translatedText: String?,
    val languageCodeFromAndTo: String
) {
    val dictionaryUrl: (String) -> String = { word ->
        "https://www.wordreference.com/${languageCodeFromAndTo.replace("_", "")}/$word"
    }
}
