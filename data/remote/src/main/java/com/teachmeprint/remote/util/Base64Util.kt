package com.teachmeprint.remote.util

import android.util.Base64

fun String.encodeBase(): String {
    val bytes = lowercase()
        .replace(" ", "")
        .toByteArray()
    val encodedBytes = Base64.encode(bytes, Base64.DEFAULT)
    return String(encodedBytes)
}