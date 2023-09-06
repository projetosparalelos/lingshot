package com.lingshot.home_domain.model

data class HomeSection(
    val typeSection: HomeTypeSection,
    val title: Int = 0
)

enum class HomeTypeSection {
    HEADER,
    COLLECTION,
    OFFENSIVE_TITLE,
    NEED_REVIEW,
    PI_CHART;
}
