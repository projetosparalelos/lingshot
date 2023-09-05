package com.lingshot.domain.model

data class GoalsDomain(
    val userId: String = "",
    val date: String = "",
    val targetPhrases: Int = 0,
    val progressPhrases: Int = 0
)
