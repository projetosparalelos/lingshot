package com.teachmeprint.languagechoice_data.repository

import com.teachmeprint.languagechoice_data.storage.LanguageChoiceLocalStorage
import com.teachmeprint.languagechoice_domain.model.AvailableLanguage
import com.teachmeprint.languagechoice_domain.repository.LanguageChoiceRepository
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