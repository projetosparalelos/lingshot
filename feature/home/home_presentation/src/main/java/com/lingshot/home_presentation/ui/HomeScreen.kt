package com.lingshot.home_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.home_presentation.ui.component.HomeDeckCard
import com.lingshot.home_presentation.ui.component.HomeNeedReviewCard
import com.lingshot.home_presentation.ui.component.HomeOffensiveTitle
import com.lingshot.home_presentation.ui.component.HomePierChartCard
import com.lingshot.home_presentation.ui.navigation.HomeDestination

@Composable
fun HomeRoute(homeDestination: HomeDestination) {
    HomeScreen(homeDestination)
}

@Composable
private fun HomeScreen(homeDestination: HomeDestination) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeOffensiveTitle()
            HomeNeedReviewCard()
            HomePierChartCard()
            Text(
                text = "Decks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            HomeDeckCard(onNavigateToCompletePhrase = homeDestination.onNavigateToCompletePhrase)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeDestination())
}
