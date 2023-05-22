package com.teachmeprint.domain.model

data class ChatGPTPromptBodyDomain(
    val model: String? = MODEL_DEFAULT,
    val prompt: String?,
    val maxTokens: Int? = MAX_TOKENS_DEFAULT,
    val temperature: Float? = TEMPERATURE_DEFAULT,
    val topP: Float? = TOP_P_DEFAULT,
    val frequencyPenalty: Int? = 0,
    val presencePenalty: Int? = 0,
    val stop: String? = STOP_DEFAULT
)

private const val MODEL_DEFAULT = "text-davinci-003"
private const val MAX_TOKENS_DEFAULT = 100
private const val TEMPERATURE_DEFAULT = 0.3f
private const val TOP_P_DEFAULT = 1F
private const val STOP_DEFAULT = "\n"
