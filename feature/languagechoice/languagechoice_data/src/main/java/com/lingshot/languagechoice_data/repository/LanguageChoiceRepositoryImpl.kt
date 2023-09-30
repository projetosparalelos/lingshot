/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lingshot.languagechoice_data.repository

import com.lingshot.languagechoice_data.storage.LanguageChoiceLocalStorage
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageChoiceRepositoryImpl @Inject constructor(
    private val languageChoiceLocalStorage: LanguageChoiceLocalStorage,
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
