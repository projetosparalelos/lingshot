package com.teachmeprint.remote.mapper

import com.teachmeprint.domain.model.ChatGPTPromptBodyDomain
import com.teachmeprint.remote.model.RequestBody

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
