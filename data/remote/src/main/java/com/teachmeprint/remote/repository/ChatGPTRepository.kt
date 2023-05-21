package com.teachmeprint.remote.repository

import com.teachmeprint.remote.api.ChatGPTService
import com.teachmeprint.remote.model.chatgpt.RequestBody
import javax.inject.Inject

class ChatGPTRepository @Inject constructor(
    private val chatGPTService: ChatGPTService,
) {

    suspend fun get(requestBody: RequestBody): String? {
        val response = chatGPTService.get(requestBody)
        return response.choices?.get(0)?.text
    }
}