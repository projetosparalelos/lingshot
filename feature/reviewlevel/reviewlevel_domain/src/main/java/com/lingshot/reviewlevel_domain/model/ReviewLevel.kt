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

package com.lingshot.reviewlevel_domain.model

import com.lingshot.reviewlevel_domain.R

enum class ReviewLevel(val level: Int, val day: Int, val label: Int) {
    NEW_WORD(level = 0, day = 0, label = R.string.text_label_new_word_review),
    UNDER_CONSTRUCTION(level = 1, day = 7, label = R.string.text_label_under_construction_review),
    CONTEXTUALIZED(level = 2, day = 15, label = R.string.text_label_contextualized_review),
    KNOWLEDGEABLE(level = 3, day = 30, label = R.string.text_label_knowledgeable_review),
    MASTER(level = 4, day = 90, label = R.string.text_label_master_review),
    ;

    companion object {
        fun from(level: Int?): ReviewLevel =
            entries.first { it.level == level }

        fun getNextReviewTimestamp(reviewLevel: ReviewLevel = NEW_WORD): Long {
            return System.currentTimeMillis() + (reviewLevel.day * 24 * 60 * 60 * 1000L)
        }
    }
}
