package com.teachmeprint.home_presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teachmeprint.designsystem.theme.LocalPieChartTheme
import com.teachmeprint.designsystem.theme.TeachMePrintTheme

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
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sua ofensiva é: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                    append("5 dias")
                }
            }
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.titleLarge
            )
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                ),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.ArrowRight, contentDescription = null
                    )
                    Text(
                        text = "5",
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "frases pendentes para ser revisadas.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            ElevatedCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PieChart(
                        chartSize = 120.dp,
                        goals = 50,
                        completed = 20
                    )
                    Column {
                        PieChartIndicator(
                            value = "50",
                            type = "goals",
                            color = LocalPieChartTheme.current.goals
                        )
                        PieChartIndicator(
                            value = "20",
                            type = "completed",
                            color = LocalPieChartTheme.current.completed
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                }
            }
            Text(
                text = "Decks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold

            )
            DeckLanguage()
        }
    }
}

@Composable
fun PieChartIndicator(value: String, type: String, color: Color) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("$value ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
            append(type)
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Text(text = text)
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    chartSize: Dp,
    goals: Int,
    completed: Int
) {
    val pieChartTheme = LocalPieChartTheme.current
    Canvas(modifier = modifier.size(chartSize)) {
        val adjustedCompleted = if (completed > goals) goals else completed
        val chartData = listOf(
            adjustedCompleted.toFloat() / goals,
            (goals - adjustedCompleted).toFloat() / goals
        )

        val chartDegrees = 360
        val canvasSize = size
        val radius = canvasSize.width / 2
        var startAngle = 0f

        chartData.indices.forEach { index ->
            val sweepAngle = chartDegrees * chartData[index]

            val color = when (index) {
                0 -> pieChartTheme.completed
                1 -> pieChartTheme.goals
                else -> Color.White
            }

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                size = Size(radius * 2, radius * 2),
                useCenter = true,
                style = Fill
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun DeckLanguage() {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                    append("\uD83C\uDDFA\uD83C\uDDF8 English ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                    append("from portuguese")
                }
            }
            Text(text = text)
            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
            Text(
                text = "Playing 0 / 12.605 sentences",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen2Preview() {
    TeachMePrintTheme {
        HomeScreen()
    }
}