package com.teachmeprint.remote.model.chatgpt

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
    val model: String? = MODEL_DEFAULT,

    @SerialName("prompt")
    val prompt: String?,

    @SerialName("max_tokens")
    val maxTokens: Int? = MAX_TOKENS_DEFAULT,

    @SerialName("temperature")
    val temperature: Float? = TEMPERATURE_DEFAULT,

    @SerialName("top_p")
    val topP: Float? = TOP_P_DEFAULT,

    @SerialName("frequency_penalty")
    val frequencyPenalty: Int? = 0,

    @SerialName("presence_penalty")
    val presencePenalty: Int? = 0,

    @SerialName("stop")
    val stop: String? = STOP_DEFAULT
)

private const val MODEL_DEFAULT = "text-davinci-003"
private const val MAX_TOKENS_DEFAULT = 100
private const val TEMPERATURE_DEFAULT = 0.3f
private const val TOP_P_DEFAULT = 1F
private const val STOP_DEFAULT = "\n"