package com.teachmeprint.remote.repository

import com.teachmeprint.domain.model.ChatGPTPromptBodyDomain
import com.teachmeprint.domain.repository.ChatGPTRepository
import com.teachmeprint.remote.api.ChatGPTService
import com.teachmeprint.remote.mapper.toRequestBodyEntity
import javax.inject.Inject

class ChatGPTRepositoryImpl @Inject constructor(
    private val chatGPTService: ChatGPTService
) : ChatGPTRepository {

    override suspend fun get(chatGPTPromptBodyDomain: ChatGPTPromptBodyDomain): String? {
        val response = chatGPTService.get(chatGPTPromptBodyDomain.toRequestBodyEntity())
        return response.choices?.get(0)?.text?.trim()
    }
}
