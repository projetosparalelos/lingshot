package com.lingshot.remote.api

import com.lingshot.remote.model.ChatGPTResponse
import com.lingshot.remote.model.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGPTService {
    @POST("completions")
    suspend fun get(@Body body: RequestBody): ChatGPTResponse
}
