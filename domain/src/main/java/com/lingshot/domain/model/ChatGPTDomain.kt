package com.lingshot.domain.model

data class MessageDomain(
    val role: String,
    val content: String
)

data class ChatGPTPromptBodyDomain(
    val model: String? = MODEL_DEFAULT,
    val messages: List<MessageDomain>,
    val temperature: Double = TEMPERATURE_DEFAULT,
    val maxTokens: Int = MAX_TOKENS_DEFAULT
)

private const val MODEL_DEFAULT = "gpt-3.5-turbo"
private const val MAX_TOKENS_DEFAULT = 100
private const val TEMPERATURE_DEFAULT = 0.1
