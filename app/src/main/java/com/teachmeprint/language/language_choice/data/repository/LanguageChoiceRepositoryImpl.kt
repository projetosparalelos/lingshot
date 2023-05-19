package com.teachmeprint.language.language_choice.data.repository

import com.teachmeprint.language.language_choice.data.storage.LanguageChoiceLocalStorage
import com.teachmeprint.language.language_choice.domain.model.AvailableLanguage
import com.teachmeprint.language.language_choice.domain.repository.LanguageChoiceRepository
import javax.inject.Inject

class LanguageChoiceRepositoryImpl @Inject constructor(
    private val languageChoiceLocalStorage: LanguageChoiceLocalStorage,
): LanguageChoiceRepository {

    override fun getLanguage(): AvailableLanguage? {
        return languageChoiceLocalStorage.getLanguage()
    }

    override fun saveLanguage(availableLanguage: AvailableLanguage?) {
        languageChoiceLocalStorage.saveLanguage(availableLanguage)
    }

    override fun deleteLanguage(): Boolean = languageChoiceLocalStorage.deleteLanguage()
}