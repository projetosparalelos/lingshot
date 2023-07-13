package com.teachmeprint.languagechoice_data.repository

import com.teachmeprint.languagechoice_data.storage.LanguageChoiceLocalStorage
import com.teachmeprint.languagechoice_domain.model.AvailableLanguage
import com.teachmeprint.languagechoice_domain.repository.LanguageChoiceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LanguageChoiceRepositoryImpl @Inject constructor(
    private val languageChoiceLocalStorage: LanguageChoiceLocalStorage
) : LanguageChoiceRepository {

    override fun getLanguage(): Flow<AvailableLanguage?> {
        return languageChoiceLocalStorage.getLanguage()
    }

    override suspend fun saveLanguage(availableLanguage: AvailableLanguage?) {
        languageChoiceLocalStorage.saveLanguage(availableLanguage)
    }

    override suspend fun deleteLanguage() {
        languageChoiceLocalStorage.deleteLanguage()
    }
}
