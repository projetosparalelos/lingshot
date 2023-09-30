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
package com.phrase.phrasemaster_domain.model

data class LanguageCollectionDomain(
    val id: String = "",
    val from: String = "",
    val to: String = "",
)

data class CollectionInfoDomain(
    val listTotalPhrases: List<Int> = emptyList(),
    val listPhrasesPlayed: List<Int> = emptyList(),
)

data class PhraseDomain(
    val id: String = "",
    val original: String = "",
    val translate: String = "",
    val reviewLevel: Int = 0,
    val nextReviewTimestamp: Long = System.currentTimeMillis(),
)
