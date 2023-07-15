@file:Suppress("Unused")

package com.lingshot.languagechoice_domain.model

enum class AvailableLanguage(
    val displayName: String,
    val languageCode: String,
    val flagEmoji: String
) {
    ENGLISH("English", "en", "\uD83C\uDDFA\uD83C\uDDF8"),
    FRENCH("French", "fr", "\uD83C\uDDEB\uD83C\uDDF7"),
    GERMAN("German", "de", "\uD83C\uDDE9\uD83C\uDDEA"),
    ITALIAN("Italian", "it", "\uD83C\uDDEE\uD83C\uDDF9"),
    PORTUGUESE("Portuguese", "pt", "\uD83C\uDDE7\uD83C\uDDF7"),
    RUSSIAN("Russian", "ru", "\uD83C\uDDF7\uD83C\uDDFA"),
    SPANISH("Spanish", "es", "\uD83C\uDDEA\uD83C\uDDF8"),
    SWEDISH("Swedish", "sv", "\uD83C\uDDF8\uD83C\uDDEA");

    companion object {
        fun from(languageCode: String?): AvailableLanguage? =
            values().firstOrNull {
                it.languageCode.equals(languageCode, ignoreCase = true)
            }
    }
}
