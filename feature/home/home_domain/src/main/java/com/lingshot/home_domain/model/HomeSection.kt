package com.lingshot.home_domain.model

data class HomeSection(
    val typeSection: HomeTypeSection,
    val title: String? = null
)

enum class HomeTypeSection {
    HEADER,
    COLLECTION,
    OFFENSIVE_TITLE,
    NEED_REVIEW,
    PI_CHART;
}
