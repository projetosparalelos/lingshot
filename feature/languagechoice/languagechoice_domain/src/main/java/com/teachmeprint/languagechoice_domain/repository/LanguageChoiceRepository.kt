package com.teachmeprint.languagechoice_domain.repository

import com.teachmeprint.languagechoice_domain.model.AvailableLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageChoiceRepository {
    fun getLanguage(): Flow<AvailableLanguage?>

    suspend fun saveLanguage(availableLanguage: AvailableLanguage?)

    suspend fun deleteLanguage()
}
