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
package com.lingshot.screenshot_domain.model

import com.lingshot.languagechoice_domain.model.AvailableLanguage

enum class DictionaryType {
    REVERSO_CONTEXT {
        override fun url(languageCodeFrom: String, languageCodeTo: String, word: String): String {
            val baseUrl = "https://context.reverso.net/translation"
            val languageFrom = AvailableLanguage.from(languageCodeFrom)?.name
            val languageTo = AvailableLanguage.from(languageCodeTo)?.name

            return "$baseUrl/$languageFrom-$languageTo/$word".removeCharactersFromStartAndEnd()
        }
    }, ;

    abstract fun url(languageCodeFrom: String, languageCodeTo: String, word: String): String

    protected fun String.removeCharactersFromStartAndEnd(): String {
        return replace(Regex("[\"']"), "")
            .replace(Regex("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+\$"), "")
            .lowercase()
            .trim()
    }
}
