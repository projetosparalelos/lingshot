package com.lingshot.remote.mapper

import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.remote.model.RequestBody

fun ChatGPTPromptBodyDomain.toRequestBodyEntity(): RequestBody {
    return RequestBody(
        model = model,
        prompt = prompt,
        maxTokens = maxTokens,
        temperature = temperature,
        topP = topP,
        frequencyPenalty = frequencyPenalty,
        presencePenalty = presencePenalty,
        stop = stop
    )
}
