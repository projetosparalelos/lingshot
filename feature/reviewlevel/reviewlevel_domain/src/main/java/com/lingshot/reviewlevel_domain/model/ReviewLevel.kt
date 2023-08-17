package com.lingshot.reviewlevel_domain.model

enum class ReviewLevel(val level: Int, val day: Int, val label: String) {
    NEW_WORD(level = 0, day = 0, label = "New Word"),
    UNDER_CONSTRUCTION(level = 1, day = 7, label = "Under Construction"),
    CONTEXTUALIZED(level = 2, day = 15, label = "Contextualized"),
    KNOWLEDGEABLE(level = 3, day = 30, label = "Knowledgeable"),
    MASTER(level = 4, day = 90, label = "Master");

    companion object {
        fun from(level: Int?): ReviewLevel? =
            values().firstOrNull { it.level == level }

        fun getNextReviewTimestamp(reviewLevel: ReviewLevel = NEW_WORD): Long {
            return System.currentTimeMillis() + (reviewLevel.day * 24 * 60 * 60 * 1000L)
        }
    }
}
