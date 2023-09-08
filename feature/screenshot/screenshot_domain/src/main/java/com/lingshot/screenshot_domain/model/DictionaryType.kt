package com.lingshot.screenshot_domain.model

import com.lingshot.languagechoice_domain.model.AvailableLanguage

enum class DictionaryType {
    REVERSO_CONTEXT {
        override fun url(languageCodeFrom: String, languageCodeTo: String, word: String): String {
            val baseUrl = "https://context.reverso.net/translation"
            val languageFrom = AvailableLanguage.from(languageCodeFrom)?.name
            val languageTo = AvailableLanguage.from(languageCodeTo)?.name

            return "$baseUrl/$languageFrom-$languageTo/$word".removeCharactersFromEnd()
        }
    };

    abstract fun url(languageCodeFrom: String, languageCodeTo: String, word: String): String

    protected fun String.removeCharactersFromEnd(): String {
        return replace(Regex("[^\\p{L}\\p{N}]+\$"), "").lowercase().trim()
    }
}
