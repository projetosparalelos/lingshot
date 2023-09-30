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
) {
    ENGLISH(R.string.text_display_name_english_language, "en", "\uD83C\uDDFA\uD83C\uDDF8"),
    FRENCH(R.string.text_display_name_french_language, "fr", "\uD83C\uDDEB\uD83C\uDDF7"),
    PORTUGUESE(R.string.text_display_name_portuguese_language, "pt", "\uD83C\uDDE7\uD83C\uDDF7"),
    SPANISH(R.string.text_display_name_spanish_language, "es", "\uD83C\uDDEA\uD83C\uDDF8"),
    SWEDISH(R.string.text_display_name_swedish_language, "sv", "\uD83C\uDDF8\uD83C\uDDEA"),
    ;

    companion object {
        fun from(languageCode: String?): AvailableLanguage? =
            entries.firstOrNull {
                it.languageCode.equals(languageCode, ignoreCase = true)
            }
    }
}
