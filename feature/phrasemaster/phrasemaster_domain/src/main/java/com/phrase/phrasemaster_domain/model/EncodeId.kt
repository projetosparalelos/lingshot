package com.phrase.phrasemaster_domain.model

import java.math.BigInteger
import java.security.MessageDigest

fun String.encodeId(): String {
    val bytes = lowercase()
        .replace(" ", "")
        .toByteArray(Charsets.UTF_8)

    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)

    val bigInt = BigInteger(1, digest)
    return bigInt.toString(36)
}
