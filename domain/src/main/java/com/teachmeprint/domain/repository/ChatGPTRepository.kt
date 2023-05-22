package com.teachmeprint.domain.repository

import com.teachmeprint.domain.model.ChatGPTPromptBodyDomain

interface ChatGPTRepository {

    suspend fun get(chatGPTPromptBodyDomain: ChatGPTPromptBodyDomain): String?
}
