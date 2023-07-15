package com.lingshot.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PieChartTheme(
    val completed: Color = Color.Unspecified,
    val goals: Color = Color.Unspecified
)

val LocalPieChartTheme = staticCompositionLocalOf { PieChartTheme() }
