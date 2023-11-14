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
package com.lingshot.remote.mapper

import com.lingshot.domain.model.ChatGPTPromptBodyDomain
import com.lingshot.domain.model.MessageDomain
import com.lingshot.remote.model.Message
import com.lingshot.remote.model.RequestBody

fun MessageDomain.toMessageEntity(): Message {
    return Message(role, content)
}

fun ChatGPTPromptBodyDomain.toRequestBodyEntity(): RequestBody {
    val messages = messages.map { it.toMessageEntity() }
    return RequestBody(
        model = model,
        messages = messages,
        temperature = temperature,
        maxTokens = maxTokens,
    )
}
