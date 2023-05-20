package com.teachmeprint.remote.api

import com.teachmeprint.remote.model.chatgpt.RequestBody
import com.teachmeprint.remote.model.chatgpt.TranslateChatGPTResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateChatGPTService {
    @POST("completions")
    suspend fun getTranslatePhrase(@Body body: RequestBody) : TranslateChatGPTResponse
}