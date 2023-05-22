package com.teachmeprint.common.helper

import com.teachmeprint.common.R

object StatusMessage {
    const val STATUS_TEXT_ERROR_GENERIC = 0
    const val STATUS_TEXT_RECOGNIZER_FAILED = 1
    const val STATUS_TEXT_TO_SPEECH_FAILED = 2
    const val STATUS_TEXT_TO_SPEECH_ERROR = 3
    const val STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED = 4
    const val STATUS_IDENTIFY_LANGUAGE_FAILED = 5

    fun getErrorMessage(statusCode: Int): Int {
        return when (statusCode) {
            STATUS_TEXT_ERROR_GENERIC -> R.string.text_status_message_error_generic
            STATUS_TEXT_RECOGNIZER_FAILED -> R.string.text_status_message_recognition_failed
            STATUS_TEXT_TO_SPEECH_FAILED -> R.string.text_status_message_speech_failed
            STATUS_TEXT_TO_SPEECH_ERROR -> R.string.text_status_message_speech_error
            STATUS_TEXT_TO_SPEECH_NOT_SUPPORTED -> R.string.text_status_message_speech_not_supported
            STATUS_IDENTIFY_LANGUAGE_FAILED -> R.string.text_status_language_identification_failed
            else -> R.string.text_status_message_processing_request
        }
    }
}
