package com.teachmeprint.remote.api

import com.teachmeprint.remote.model.chatgpt.ChatGPTResponse
import com.teachmeprint.remote.model.chatgpt.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGPTService {
    @POST("completions")
    suspend fun get(@Body body: RequestBody) : ChatGPTResponse
}