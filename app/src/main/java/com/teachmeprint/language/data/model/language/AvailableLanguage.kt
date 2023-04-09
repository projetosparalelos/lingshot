@file:Suppress("Unused")

package com.teachmeprint.language.data.model.language

enum class AvailableLanguage(
    val displayName: String,
    val languageCode: String
) {
    ARABIC("Arabic", "ar"),
    CHINESE("Chinese", "zh"),
    ENGLISH("English", "en"),
    FRENCH("French", "fr"),
    GERMAN("German", "de"),
    HEBREW("Hebrew", "he"),
    ITALIAN("Italian", "it"),
    JAPANESE("Japanese", "ja"),
    KOREAN("Korean", "ko"),
    PORTUGUESE("Portuguese", "br"),
    RUSSIAN("Russian", "ru"),
    SPANISH("Spanish", "es")
}