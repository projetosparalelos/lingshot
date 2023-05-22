package com.teachmeprint.languagechoice_domain.repository

import com.teachmeprint.languagechoice_domain.model.AvailableLanguage

interface LanguageChoiceRepository {
    fun getLanguage(): AvailableLanguage?

    fun saveLanguage(availableLanguage: AvailableLanguage?)

    fun deleteLanguage(): Boolean
}
