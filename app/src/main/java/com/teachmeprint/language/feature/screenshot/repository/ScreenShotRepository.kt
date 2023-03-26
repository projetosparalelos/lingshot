package com.teachmeprint.language.feature.screenshot.repository

import com.teachmeprint.language.data.local.storage.LanguageLocalStorage
import com.teachmeprint.language.data.model.screenshot.entity.RequestBody
import com.teachmeprint.language.data.model.screenshot.entity.TranslateChatGPTResponse
import com.teachmeprint.language.data.remote.api.TranslateChatGPTService

class ScreenShotRepository(private val translateChatGPTService: TranslateChatGPTService,
                           private val languageLocalStorage: LanguageLocalStorage) {

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