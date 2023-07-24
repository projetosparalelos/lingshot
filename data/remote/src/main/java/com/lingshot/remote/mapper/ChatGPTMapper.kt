package com.lingshot.remote.mapper

import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.MessageDomain
import com.lingshot.remote.model.Message
import com.lingshot.remote.model.RequestBody

fun MessageDomain.toMessageEntity(): Message {
    return Message(role, content)
}

fun ChatGPTPromptBodyDomain.toRequestBodyEntity(): RequestBody {
    val messages = messages.map { it.toMessageEntity() }
    return RequestBody(
        model = model,
        messages = messages,
        temperature = temperature,
        max_tokens = maxTokens
    )
}
