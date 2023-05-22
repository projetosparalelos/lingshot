@file:Suppress("Unused")

package com.teachmeprint.languagechoice_domain.model

enum class AvailableLanguage(
    val displayName: String,
    val languageCode: String,
    _flag: String
) {
    ENGLISH("English", "en", "tn_us-flag.gif"),
    FRENCH("French", "fr", "tn_fr-flag.gif"),
    GERMAN("German", "de", "tn_gm-flag.gif"),
    ITALIAN("Italian", "it", "tn_it-flag.gif"),
    KOREAN("Korean", "ko", "tn_ks-flag.gif"),
    PORTUGUESE("Portuguese", "br", "tn_br-flag.gif"),
    RUSSIAN("Russian", "ru", "tn_rs-flag.gif"),
    SPANISH("Spanish", "es", "tn_sp-flag.gif");

    val flag = "https://www.worldometers.info/img/flags/small/$_flag"
}
