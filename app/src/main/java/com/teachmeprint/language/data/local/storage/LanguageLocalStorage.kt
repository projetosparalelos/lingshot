@file:Suppress("Unused")

package com.teachmeprint.language.data.local.storage

import com.orhanobut.hawk.Hawk
import com.teachmeprint.language.data.model.language.AvailableLanguage
import javax.inject.Inject

class LanguageLocalStorage @Inject constructor() {

    fun getLanguage(): AvailableLanguage? = runCatching {
        Hawk.get<AvailableLanguage?>(LANGUAGE_DATA_KEY)
    }.getOrNull()

    fun saveLanguage(availableLanguage: AvailableLanguage?) =
        Hawk.put(LANGUAGE_DATA_KEY, availableLanguage)

    fun deleteLanguage() = Hawk.delete(LANGUAGE_DATA_KEY)

    companion object {
        private const val LANGUAGE_DATA_KEY: String = "LANGUAGE_DATA_KEY"
    }
}