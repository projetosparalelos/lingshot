package com.teachmeprint.language.data.remote.api

import com.teachmeprint.language.data.model.screenshot.entity.RequestBody
import com.teachmeprint.language.data.model.screenshot.entity.TranslateChatGPTResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateChatGPTService {
    @POST("completions")
    suspend fun getTranslatePhrase(@Body body: RequestBody) : TranslateChatGPTResponse
}