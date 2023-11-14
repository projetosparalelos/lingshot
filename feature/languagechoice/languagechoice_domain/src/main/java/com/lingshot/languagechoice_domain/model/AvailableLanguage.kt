/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("Unused")

package com.lingshot.languagechoice_domain.model

import com.lingshot.languagechoice_domain.R

enum class AvailableLanguage(
    val displayName: Int,
    val languageCode: String,
    val flagEmoji: String,
    val enabledDictionary: Boolean = true,
    val enabledLanguageFrom: Boolean = true,
    val enabledLanguage: Boolean = true,
) {
    CHINESE(displayName = R.string.text_display_name_chinese_language, languageCode = "zh", flagEmoji = "\uD83C\uDDE8\uD83C\uDDF3", enabledDictionary = false, enabledLanguage = false),
    ENGLISH(displayName = R.string.text_display_name_english_language, languageCode = "en", flagEmoji = "\uD83C\uDDFA\uD83C\uDDF8"),
    FRENCH(displayName = R.string.text_display_name_french_language, languageCode = "fr", flagEmoji = "\uD83C\uDDEB\uD83C\uDDF7"),
    GERMAN(displayName = R.string.text_display_name_german_language, languageCode = "de", flagEmoji = "\uD83C\uDDE9\uD83C\uDDEA"),
    ITALIAN(displayName = R.string.text_display_name_italian_language, languageCode = "it", flagEmoji = "\uD83C\uDDEE\uD83C\uDDF9"),
    JAPANESE(displayName = R.string.text_display_name_japanese_language, languageCode = "ja", flagEmoji = "\uD83C\uDDEF\uD83C\uDDF5", enabledDictionary = false, enabledLanguage = false),
    KOREAN(displayName = R.string.text_display_name_korean_language, languageCode = "ko", flagEmoji = "\uD83C\uDDF0\uD83C\uDDF7", enabledDictionary = false, enabledLanguage = false),
    PORTUGUESE(displayName = R.string.text_display_name_portuguese_language, languageCode = "pt", flagEmoji = "\uD83C\uDDE7\uD83C\uDDF7"),
    RUSSIAN(displayName = R.string.text_display_name_russian_language, languageCode = "ru", flagEmoji = "\uD83C\uDDF7\uD83C\uDDFA", enabledDictionary = false, enabledLanguageFrom = false),
    SPANISH(displayName = R.string.text_display_name_spanish_language, languageCode = "es", flagEmoji = "\uD83C\uDDEA\uD83C\uDDF8"),
    SWEDISH(displayName = R.string.text_display_name_swedish_language, languageCode = "sv", flagEmoji = "\uD83C\uDDF8\uD83C\uDDEA"),
    ;

    companion object {
        fun from(languageCode: String?): AvailableLanguage? =
            values().firstOrNull {
                it.languageCode.equals(languageCode, ignoreCase = true)
            }
    }
}
