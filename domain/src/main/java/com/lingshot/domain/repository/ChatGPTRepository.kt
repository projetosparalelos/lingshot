package com.lingshot.domain.repository

import com.lingshot.domain.model.ChatGPTPromptBodyDomain

interface ChatGPTRepository {

    suspend fun get(chatGPTPromptBodyDomain: ChatGPTPromptBodyDomain): String?
}
