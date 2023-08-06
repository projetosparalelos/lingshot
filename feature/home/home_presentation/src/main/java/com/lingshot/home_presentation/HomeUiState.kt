package com.lingshot.home_presentation

import com.lingshot.domain.model.UserDomain
import com.lingshot.home_domain.model.HomeSection
import com.lingshot.home_domain.model.HomeTypeSection
import kotlinx.collections.immutable.toImmutableList

data class HomeUiState(
    val userDomain: UserDomain? = null
) {

    val homeSection = listOf(
        HomeSection(HomeTypeSection.OFFENSIVE_TITLE),
        HomeSection(HomeTypeSection.NEED_REVIEW),
        HomeSection(HomeTypeSection.PI_CHART),
        HomeSection(HomeTypeSection.HEADER, "Decks"),
        HomeSection(HomeTypeSection.DECKS)
    ).toImmutableList()
}
