/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lingshot.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChatGPTResponse(
    @SerializedName("choices")
    val choices: List<Choice>,
)

@Keep
data class Choice(
    @SerializedName("message")
    val message: Message,
)

@Keep
data class Message(
    @SerializedName("role")
    val role: String?,

    @SerializedName("content")
    val content: String?,
)

@Keep
data class RequestBody(
    @SerializedName("model")
    val model: String?,

    @SerializedName("messages")
    val messages: List<Message>,

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("max_tokens")
    val maxTokens: Int,
)
