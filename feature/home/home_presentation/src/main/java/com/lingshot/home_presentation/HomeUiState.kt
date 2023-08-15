package com.lingshot.home_presentation

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.statusDefault
import com.lingshot.home_domain.model.HomeSection
import com.lingshot.home_domain.model.HomeTypeSection
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import kotlinx.collections.immutable.toImmutableList

data class HomeUiState(
    val userDomain: UserDomain? = null,
    val languageCollectionsStatus: Status<List<LanguageCollectionDomain>> =
        statusDefault()
) {

    val homeSection = listOf(
        HomeSection(HomeTypeSection.OFFENSIVE_TITLE),
        HomeSection(HomeTypeSection.NEED_REVIEW),
        HomeSection(HomeTypeSection.PI_CHART),
        HomeSection(HomeTypeSection.HEADER, "Collections"),
        HomeSection(HomeTypeSection.COLLECTION)
    ).toImmutableList()
}