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
package com.lingshot.remote.repository

import com.lingshot.domain.repository.GoogleTranslateRepository
import com.lingshot.remote.api.GoogleTranslateService
import javax.inject.Inject

class GoogleTranslateRepositoryImpl @Inject constructor(
    private val googleTranslateService: GoogleTranslateService,
) : GoogleTranslateRepository {

    override suspend fun get(text: String, languageFrom: String, languageTo: String): String? {
        val response = googleTranslateService.get(text = text, languageFrom = languageFrom, languageTo = languageTo)
        return response.data.translations[0].translatedText?.trim()
    }
}
