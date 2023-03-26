package com.language.teachermetoon.core.helper

object StatusMessage {
    const val STATUS_TEXT_ERROR_GENERIC = 0
    const val STATUS_TEXT_RECOGNIZER_FAILED = 1
    const val STATUS_TEXT_TO_SPEECH_FAILED = 2
    const val STATUS_TEXT_TO_SPEECH_ERROR = 3
    const val STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED = 4
    const val STATUS_IDENTIFY_LANGUAGE_FAILED = 5

    fun getErrorMessage(statusCode: Int): String {
        return when (statusCode) {
            STATUS_TEXT_ERROR_GENERIC -> "An unknown error occurred."
            STATUS_TEXT_RECOGNIZER_FAILED -> "Text recognition failed."
            STATUS_TEXT_TO_SPEECH_FAILED -> "Text to speech failed."
            STATUS_TEXT_TO_SPEECH_ERROR -> "An error occurred while using text to speech."
            STATUS_IDENTIFY_LANGUAGE_FAILED -> "Language identification failed."
            STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED -> "Text to speech not supported."
            else -> "An error occurred while processing your request."
        }
    }
}