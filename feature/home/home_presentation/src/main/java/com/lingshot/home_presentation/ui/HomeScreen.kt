package com.lingshot.home_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.common.helper.onEmpty
import com.lingshot.common.helper.onLoading
import com.lingshot.common.helper.onSuccess
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder
import com.lingshot.home_domain.model.HomeTypeSection
import com.lingshot.home_presentation.HomeEvent
import com.lingshot.home_presentation.HomeEvent.SaveGoals
import com.lingshot.home_presentation.HomeEvent.SelectedGoalDays
import com.lingshot.home_presentation.HomeEvent.SignOut
import com.lingshot.home_presentation.HomeEvent.ToggleExpandDropdownMenuSignOut
import com.lingshot.home_presentation.HomeEvent.ToggleSetGoalsDialog
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.HomeViewModel
import com.lingshot.home_presentation.navigation.HomeDestination
import com.lingshot.home_presentation.ui.component.HomeCollectionCard
import com.lingshot.home_presentation.ui.component.HomeEmptyCollectionCard
import com.lingshot.home_presentation.ui.component.HomeNeedReviewCard
import com.lingshot.home_presentation.ui.component.HomeOffensiveTitle
import com.lingshot.home_presentation.ui.component.HomePierChartCard
import com.lingshot.home_presentation.ui.component.HomeToolbar
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain

@Composable
internal fun HomeRoute(
    homeDestination: HomeDestination,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        homeDestination = homeDestination,
        handleEvent = viewModel::handleEvent,
        uiState = uiState
    )
}

@Composable
private fun HomeScreen(
    homeDestination: HomeDestination,
    handleEvent: (HomeEvent) -> Unit,
    uiState: HomeUiState
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeToolbar(
                userDomain = uiState.userDomain,
                isExpandedDropdownMenuSignOut = uiState.isExpandedDropdownMenuSignOut,
                onToggleExpandDropdownMenuSignOut = {
                    handleEvent(ToggleExpandDropdownMenuSignOut)
                },
                onSignOut = {
                    handleEvent(SignOut(homeDestination.onSignOut))
                }
            )
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.homeSection) { section ->
                    when (section.typeSection) {
                        HomeTypeSection.HEADER -> {
                            Text(
                                text = stringResource(id = section.title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HomeTypeSection.OFFENSIVE_TITLE -> {
                            uiState.consecutiveDaysStatus.onLoading {
                                HomeOffensiveTitle(
                                    modifier = Modifier.placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.fade()
                                    ),
                                    consecutiveDays = 0
                                )
                            }.onSuccess { data ->
                                HomeOffensiveTitle(consecutiveDays = data)
                            }
                        }

                        HomeTypeSection.NEED_REVIEW -> {
                            uiState.phrasesPendingReviewStatus
                                .onLoading {
                                    HomeNeedReviewCard(
                                        modifier = Modifier.placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.fade()
                                        ),
                                        pendingReview = null
                                    )
                                }.onSuccess { data ->
                                    HomeNeedReviewCard(
                                        pendingReview = data
                                    )
                                }
                        }

                        HomeTypeSection.PI_CHART -> {
                            HomePierChartCard(
                                modifier = Modifier.placeholder(
                                    visible = uiState.isPieChartGoalsVisible.not(),
                                    highlight = PlaceholderHighlight.fade()
                                ),
                                goalsDomain = uiState.goalsDomain,
                                isSetGoalsDialogVisible = uiState.isSetGoalsDialogVisible,
                                selectedGoalDays = uiState.selectedGoalDays,
                                listCountPhrases = uiState.goalDaysList,
                                onSelectedGoalDays = {
                                    handleEvent(SelectedGoalDays(it))
                                },
                                onSaveGoals = {
                                    handleEvent(SaveGoals(it))
                                },
                                onToggleSetGoalsDialog = {
                                    handleEvent(ToggleSetGoalsDialog)
                                }
                            )
                        }

                        HomeTypeSection.COLLECTION -> {
                            uiState.languageCollectionsStatus
                                .onLoading {
                                    HomeCollectionCard(
                                        modifier = Modifier.placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.fade()
                                        ),
                                        languageCollectionDomain = LanguageCollectionDomain(),
                                        totalPhrases = 0,
                                        phrasesPlayed = 0,
                                        onNavigateToCompletePhrase = { _, _, _ -> }
                                    )
                                }
                                .onEmpty {
                                    HomeEmptyCollectionCard()
                                }
                                .onSuccess { data ->
                                    data.first.fastForEachIndexed { position, item ->
                                        HomeCollectionCard(
                                            languageCollectionDomain = item,
                                            totalPhrases = data.second.listTotalPhrases[position],
                                            phrasesPlayed = data.second.listPhrasesPlayed[position],
                                            onNavigateToCompletePhrase = homeDestination.onNavigateToCompletePhrase
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        homeDestination = HomeDestination(),
        handleEvent = {},
        uiState = HomeUiState()
    )
}
