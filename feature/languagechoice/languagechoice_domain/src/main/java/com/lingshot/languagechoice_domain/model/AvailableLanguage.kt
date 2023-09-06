@file:Suppress("Unused")

package com.lingshot.languagechoice_domain.model

import com.lingshot.languagechoice_domain.R

enum class AvailableLanguage(
    val displayName: Int,
    val languageCode: String,
    val flagEmoji: String
) {
    ENGLISH(R.string.text_display_name_english_language, "en", "\uD83C\uDDFA\uD83C\uDDF8"),
    FRENCH(R.string.text_display_name_french_language, "fr", "\uD83C\uDDEB\uD83C\uDDF7"),
    GERMAN(R.string.text_display_name_german_language, "de", "\uD83C\uDDE9\uD83C\uDDEA"),
    ITALIAN(R.string.text_display_name_italian_language, "it", "\uD83C\uDDEE\uD83C\uDDF9"),
    PORTUGUESE(R.string.text_display_name_portuguese_language, "pt", "\uD83C\uDDE7\uD83C\uDDF7"),
    RUSSIAN(R.string.text_display_name_russian_language, "ru", "\uD83C\uDDF7\uD83C\uDDFA"),
    SPANISH(R.string.text_display_name_spanish_language, "es", "\uD83C\uDDEA\uD83C\uDDF8"),
    SWEDISH(R.string.text_display_name_swedish_language, "sv", "\uD83C\uDDF8\uD83C\uDDEA");

    companion object {
        fun from(languageCode: String?): AvailableLanguage? =
            entries.firstOrNull {
                it.languageCode.equals(languageCode, ignoreCase = true)
            }
    }
}
