package com.lingshot.home_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.home_domain.model.HomeTypeSection
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.HomeViewModel
import com.lingshot.home_presentation.ui.component.HomeDeckCard
import com.lingshot.home_presentation.ui.component.HomeNeedReviewCard
import com.lingshot.home_presentation.ui.component.HomeOffensiveTitle
import com.lingshot.home_presentation.ui.component.HomePierChartCard
import com.lingshot.home_presentation.ui.component.HomeToolbar
import com.lingshot.home_presentation.ui.navigation.HomeDestination

@Composable
fun HomeRoute(
    homeDestination: HomeDestination,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        homeDestination = homeDestination,
        uiState = uiState
    )
}

@Composable
private fun HomeScreen(
    homeDestination: HomeDestination,
    uiState: HomeUiState
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeToolbar(userDomain = uiState.userDomain)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.homeSection) { section ->
                    when (section.typeSection) {
                        HomeTypeSection.HEADER -> {
                            Text(
                                text = section.title.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HomeTypeSection.OFFENSIVE_TITLE -> {
                            HomeOffensiveTitle()
                        }

                        HomeTypeSection.NEED_REVIEW -> {
                            HomeNeedReviewCard()
                        }

                        HomeTypeSection.PI_CHART -> {
                            HomePierChartCard()
                        }
                        HomeTypeSection.DECKS -> {
                            HomeDeckCard(
                                onNavigateToCompletePhrase =
                                homeDestination.onNavigateToCompletePhrase
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        homeDestination = HomeDestination(),
        uiState = HomeUiState()
    )
}
