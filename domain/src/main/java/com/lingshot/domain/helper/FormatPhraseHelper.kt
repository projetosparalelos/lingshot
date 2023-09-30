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
package com.lingshot.domain.helper

object FormatPhraseHelper {

    private val regexDoubleParentheses get() = Regex("\\(\\((.*?)\\)\\)")

    fun removeDoubleParenthesesAllPhrase(text: String): String {
        return text.replace(regexDoubleParentheses, "$1")
    }

    fun extractWordsInDoubleParentheses(text: String): String {
        val matches = regexDoubleParentheses.findAll(text)
        return matches.map { it.groupValues[1] }.joinToString(" ")
    }

    fun processPhraseWithDoubleParentheses(text: String): List<String> {
        return text.replace("((", " ((")
            .replace("))", ")) ")
            .replace(Regex("\\s+"), " ")
            .split(" ")
    }
}
