package com.teachmeprint.remote.repository

import com.teachmeprint.remote.api.TranslateChatGPTService
import com.teachmeprint.remote.model.chatgpt.RequestBody
import javax.inject.Inject

class TranslateChatGPTRepository @Inject constructor(
    private val translateChatGPTService: TranslateChatGPTService,
) {

    suspend fun getTranslate(requestBody: RequestBody): String? {
        val response = translateChatGPTService.getTranslatePhrase(requestBody)
        return response.choices?.get(0)?.text
    }
}