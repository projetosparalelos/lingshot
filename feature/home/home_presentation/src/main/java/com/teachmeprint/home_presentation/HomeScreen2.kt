package com.teachmeprint.home_presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.designsystem.theme.completed_pieChart
import com.teachmeprint.designsystem.theme.goals_pieChart

@Composable
fun HomeScreen2() {
    TeachMePrintTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    shape = CircleShape
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier)
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
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier)
                        PieChart(
                            chartSize = 120.dp,
                            goals = 50,
                            completed = 20
                        )
                        Column {
                            PieChartIndicator(value = "50", type = "goals", color = goals_pieChart)
                            PieChartIndicator(
                                value = "20",
                                type = "completed",
                                color = completed_pieChart
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
                0 -> completed_pieChart
                1 -> goals_pieChart
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
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp),
                painter = painterResource(id = R.drawable.usa_flag),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append("English ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                        append("from portuguese")
                    }
                }

                Text(text = text)
                LinearProgressIndicator(
                    progress = 0.6f, // Defina o valor do progresso aqui (0.6f = 60%)
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = completed_pieChart, // Cor do progresso
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen2Preview() {
    TeachMePrintTheme {
        HomeScreen2()
    }
}