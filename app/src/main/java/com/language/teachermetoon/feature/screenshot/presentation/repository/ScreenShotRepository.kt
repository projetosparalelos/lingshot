package com.language.teachermetoon.feature.screenshot.presentation.repository

import com.language.teachermetoon.data.local.storage.LanguageLocalStorage
import com.language.teachermetoon.data.model.screenshot.entity.RequestBody
import com.language.teachermetoon.data.model.screenshot.entity.TranslateChatGPTResponse
import com.language.teachermetoon.data.remote.api.TranslateChatGPTService

class ScreenShotRepository(private val translateChatGPTService: TranslateChatGPTService, private val languageLocalStorage: LanguageLocalStorage) {

    suspend fun getTranslatePhrase(message: RequestBody): TranslateChatGPTResponse {
        return translateChatGPTService.getTranslatePhrase(message)
    }

    fun getLanguage(): String? {
        return languageLocalStorage.getLanguage()
    }

    fun saveLanguage(languageSelected: String) {
        languageLocalStorage.saveLanguage(languageSelected)
    }
}