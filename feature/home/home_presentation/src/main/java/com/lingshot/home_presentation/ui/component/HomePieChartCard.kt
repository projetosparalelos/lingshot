package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.designsystem.theme.LocalSchemeCustom
import com.lingshot.home_presentation.R

@Composable
fun HomePierChartCard(modifier: Modifier = Modifier) {
    ElevatedCard {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomePieChart(
                modifier = Modifier.size(120.dp)
            )
            Column {
                HomePieChartIndicator(
                    value = "50",
                    type = stringResource(R.string.text_label_piechart_goals_home),
                    color = LocalSchemeCustom.current.goalsPieChart
                )
                HomePieChartIndicator(
                    value = "20",
                    type = stringResource(R.string.text_label_piechart_completed_home),
                    color = LocalSchemeCustom.current.completedPieChart
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    }
}

@Composable
private fun HomePieChart(
    modifier: Modifier = Modifier,
    goals: Int = 50,
    completed: Int = 20
) {
    val schemeCustom = LocalSchemeCustom.current
    Canvas(modifier = modifier) {
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
                0 -> schemeCustom.completedPieChart
                1 -> schemeCustom.goalsPieChart
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
private fun HomePieChartIndicator(value: String, type: String, color: Color) {
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

@Preview(showBackground = true)
@Composable
private fun HomePieChartCardPreview() {
    HomePierChartCard()
}
