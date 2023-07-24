package com.lingshot.remote.model

import kotlinx.serialization.*

@Serializable
data class ChatGPTResponse(
    @SerialName("choices")
    val choices: List<Choice>
)

@Serializable
data class Choice(
    @SerialName("message")
    val message: Message
)

@Serializable
data class Message(
    @SerialName("role")
    val role: String?,

    @SerialName("content")
    val content: String?
)

@Serializable
data class RequestBody(
    @SerialName("model")
    val model: String?,

    @SerialName("messages")
    val messages: List<Message>,

    @SerialName("temperature")
    val temperature: Double,

    @SerialName("max_tokens")
    val max_tokens: Int
)
