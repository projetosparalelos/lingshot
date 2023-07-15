package com.lingshot.remote.repository

import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.repository.ChatGPTRepository
import com.lingshot.remote.api.ChatGPTService
import com.lingshot.remote.mapper.toRequestBodyEntity
import javax.inject.Inject

class ChatGPTRepositoryImpl @Inject constructor(
    private val chatGPTService: ChatGPTService
) : ChatGPTRepository {

    override suspend fun get(chatGPTPromptBodyDomain: ChatGPTPromptBodyDomain): String? {
        val response = chatGPTService.get(chatGPTPromptBodyDomain.toRequestBodyEntity())
        return response.choices?.get(0)?.text?.trim()
    }
}
