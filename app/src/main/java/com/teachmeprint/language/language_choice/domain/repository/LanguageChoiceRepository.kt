package com.teachmeprint.language.language_choice.domain.repository

import com.teachmeprint.language.language_choice.domain.model.AvailableLanguage

interface LanguageChoiceRepository {
    fun getLanguage(): AvailableLanguage?

    fun saveLanguage(availableLanguage: AvailableLanguage?)

    fun deleteLanguage(): Boolean
}