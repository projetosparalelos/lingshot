package com.teachmeprint.language.core.data.remote.api

import com.teachmeprint.language.core.data.model.chatgpt.RequestBody
import com.teachmeprint.language.core.data.model.chatgpt.TranslateChatGPTResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateChatGPTService {
    @POST("completions")
    suspend fun getTranslatePhrase(@Body body: RequestBody) : TranslateChatGPTResponse
}