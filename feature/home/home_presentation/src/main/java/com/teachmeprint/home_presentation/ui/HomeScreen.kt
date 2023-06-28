package com.teachmeprint.home_presentation.ui

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
import com.teachmeprint.home_presentation.ui.component.HomeDeckCard
import com.teachmeprint.home_presentation.ui.component.HomeNeedReviewCard
import com.teachmeprint.home_presentation.ui.component.HomeOffensiveTitle
import com.teachmeprint.home_presentation.ui.component.HomePierChartCard

@Composable
fun HomeRoute() {
    HomeScreen()
}

@Composable
private fun HomeScreen() {
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
            HomeDeckCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}