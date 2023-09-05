package com.lingshot.home_presentation

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.statusLoading
import com.lingshot.home_domain.model.HomeSection
import com.lingshot.home_domain.model.HomeTypeSection
import com.phrase.phrasemaster_domain.model.CollectionInfoDomain
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import kotlinx.collections.immutable.toImmutableList

data class HomeUiState(
    val userDomain: UserDomain? = null,
    val isExpandedDropdownMenuSignOut: Boolean = false,
    val isPieChartGoalsVisible: Boolean = false,
    val isSetGoalsDialogVisible: Boolean = false,
    val selectedGoalDays: Int = 1,
    val languageCollectionsStatus: Status<Pair<List<LanguageCollectionDomain>, CollectionInfoDomain>> =
        statusLoading(),
    val phrasesPendingReviewStatus: Status<String> = statusLoading(),
    val consecutiveDaysStatus: Status<Int> = statusLoading(),
    val goalsDomain: GoalsDomain? = null
) {

    val homeSection = listOf(
        HomeSection(HomeTypeSection.OFFENSIVE_TITLE),
        HomeSection(HomeTypeSection.NEED_REVIEW),
        HomeSection(HomeTypeSection.PI_CHART),
        HomeSection(HomeTypeSection.HEADER, "Collections"),
        HomeSection(HomeTypeSection.COLLECTION)
    ).toImmutableList()

    val goalDaysList = listOf(1, 5, 10, 20, 50, 100).toImmutableList()
}
