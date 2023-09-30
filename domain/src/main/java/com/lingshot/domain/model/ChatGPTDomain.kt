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
package com.lingshot.domain.model

data class MessageDomain(
    val role: String,
    val content: String,
)

data class ChatGPTPromptBodyDomain(
    val model: String? = MODEL_DEFAULT,
    val messages: List<MessageDomain>,
    val temperature: Double = TEMPERATURE_DEFAULT,
    val maxTokens: Int = MAX_TOKENS_DEFAULT,
)

private const val MODEL_DEFAULT = "gpt-3.5-turbo"
private const val MAX_TOKENS_DEFAULT = 100
private const val TEMPERATURE_DEFAULT = 0.1
