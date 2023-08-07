package com.lingshot.domain.helper

import android.util.Base64

fun String.encodeBase(): String {
    val bytes = lowercase()
        .replace(" ", "")
        .toByteArray()
    val encodedBytes = Base64.encode(bytes, Base64.DEFAULT)
    return String(encodedBytes)
}
