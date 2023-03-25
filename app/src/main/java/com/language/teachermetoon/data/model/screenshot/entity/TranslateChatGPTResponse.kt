package com.language.teachermetoon.data.model.screenshot.entity

import com.google.gson.annotations.SerializedName

data class TranslateChatGPTResponse(
    @SerializedName("choices")
    val choices: List<Choice>,

    @SerializedName("created")
    val created: Int,

    @SerializedName("id")
    val id: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("object")
    val `object`: String,

    @SerializedName("usage")
    val usage: Usage
)

data class Choice(
    @SerializedName("finish_reason")
    val finish_reason: String,

    @SerializedName("index")
    val index: Int,

    @SerializedName("logprobs")
    val logprobs: Any,

    @SerializedName("text")
    val text: String
)

data class Usage(
    @SerializedName("completion_tokens")
    val completion_tokens: Int,

    @SerializedName("prompt_tokens")
    val prompt_tokens: Int,

    @SerializedName("total_tokens")
    val total_tokens: Int
)

data class RequestBody(
    @SerializedName("model")
    val model: String,

    @SerializedName("prompt")
    val prompt: String,

    @SerializedName("max_tokens")
    val maxTokens: Int,

    @SerializedName("temperature")
    var temperature: Float,

    @SerializedName("top_p")
    val topP: Float,

    @SerializedName("frequency_penalty")
    val frequencyPenalty: Int,

    @SerializedName("presence_penalty")
    val presencePenalty: Int,

    @SerializedName("stop")
    val stop: String
)
