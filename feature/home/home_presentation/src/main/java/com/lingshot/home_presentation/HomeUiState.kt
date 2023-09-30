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

package com.lingshot.home_presentation

import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.UserLocalDomain
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
    val goals: Pair<UserLocalDomain?, GoalsDomain?>? = null,
) {

    val homeSection = listOf(
        HomeSection(HomeTypeSection.OFFENSIVE_TITLE),
        HomeSection(HomeTypeSection.NEED_REVIEW),
        HomeSection(HomeTypeSection.PI_CHART),
        HomeSection(HomeTypeSection.HEADER, R.string.text_label_header_collections_home),
        HomeSection(HomeTypeSection.COLLECTION),
    ).toImmutableList()

    val goalDaysList = listOf(1, 5, 10, 20, 50, 100).toImmutableList()
}
