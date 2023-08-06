package com.lingshot.completephrase_presentation

enum class ReviewLevel(val level: Int, val label: String) {
    NewWord(0, "New Word"),
    UnderConstruction(1, "Under Construction"),
    Contextualized(2, "Contextualized"),
    Knowledgeable(3, "Knowledgeable"),
    Master(4, "Master");

    companion object {
        fun from(level: Int?): ReviewLevel? =
            values().firstOrNull { it.level == level }
    }
}
