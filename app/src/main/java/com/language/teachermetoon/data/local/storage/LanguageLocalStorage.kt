@file:Suppress("Unused")
package com.language.teachermetoon.data.local.storage

import com.orhanobut.hawk.Hawk

class LanguageLocalStorage {

    fun getLanguage(): String? = Hawk.get(LANGUAGE_DATA_KEY)
    fun saveLanguage(languageSelected: String) = Hawk.put(LANGUAGE_DATA_KEY, languageSelected)
    fun deleteLanguage() = Hawk.delete(LANGUAGE_DATA_KEY)

    companion object {
        private const val LANGUAGE_DATA_KEY: String = "LANGUAGE_DATA_KEY"
    }
}