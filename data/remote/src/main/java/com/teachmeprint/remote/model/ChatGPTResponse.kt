package com.teachmeprint.remote.model

import kotlinx.serialization.*

@Serializable
data class ChatGPTResponse(
    @SerialName("choices")
    val choices: List<Choice>?,

    @SerialName("created")
    val created: Int?,

    @SerialName("id")
    val id: String?,

    @SerialName("model")
    val model: String?,

    @SerialName("object")
    val `object`: String?,

    @SerialName("usage")
    val usage: Usage?
)

@Serializable
data class Choice(
    @SerialName("finish_reason")
    val finish_reason: String?,

    @SerialName("logprobs")
    val logprobs: Int?,

    @SerialName("index")
    val index: Int?,

    @SerialName("text")
    val text: String?
)

@Serializable
data class Usage(
    @SerialName("completion_tokens")
    val completion_tokens: Int?,

    @SerialName("prompt_tokens")
    val prompt_tokens: Int?,

    @SerialName("total_tokens")
    val total_tokens: Int?
)

@Serializable
data class RequestBody(
    @SerialName("model")
    val model: String?,

    @SerialName("prompt")
    val prompt: String?,

    @SerialName("max_tokens")
    val maxTokens: Int?,

    @SerialName("temperature")
    val temperature: Float?,

    @SerialName("top_p")
    val topP: Float?,

    @SerialName("frequency_penalty")
    val frequencyPenalty: Int?,

    @SerialName("presence_penalty")
    val presencePenalty: Int?,

    @SerialName("stop")
    val stop: String?
)