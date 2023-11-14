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
package com.lingshot.domain.usecase

import com.lingshot.domain.PromptChatGPTConstant.PROMPT_TRANSLATE
import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.MessageDomain
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.domain.repository.GoogleTranslateRepository
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.FROM
import com.lingshot.languagechoice_domain.model.TranslateLanguageType.TO
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TranslateApiUseCase @Inject constructor(
    private val chatGPTRepository: ChatGPTRepository,
    private val googleTranslateRepository: GoogleTranslateRepository,
    private val languageChoiceRepository: LanguageChoiceRepository,
) {

    suspend operator fun invoke(text: String): String? {
        return if (getLanguageFrom()?.enabledLanguage == true) {
            val requestBody = ChatGPTPromptBodyDomain(
                messages = listOf(
                    MessageDomain(
                        role = "system",
                        content = PROMPT_TRANSLATE(
                            getLanguageFrom()?.name?.lowercase(),
                            getLanguageTo()?.name?.lowercase(),
                        ),
                    ),
                    MessageDomain(
                        role = "user",
                        content = text,
                    ),
                ),
            )
            chatGPTRepository.get(requestBody)
        } else {
            googleTranslateRepository.get(
                text = text,
                languageFrom = getLanguageFrom()?.languageCode.toString(),
                languageTo = getLanguageTo()?.languageCode.toString(),
            )
        }
    }

    private suspend fun getLanguageTo(): AvailableLanguage? {
        return languageChoiceRepository.getLanguage(TO).first()
    }

    private suspend fun getLanguageFrom(): AvailableLanguage? {
        return languageChoiceRepository.getLanguage(FROM).first()
    }
}
