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
