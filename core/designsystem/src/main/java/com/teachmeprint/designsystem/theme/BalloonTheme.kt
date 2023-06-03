package com.teachmeprint.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class BalloonTheme(
    val content: Color = Color.Unspecified,
    val onContent: Color = Color.Unspecified
)

val LocalBalloonTheme = staticCompositionLocalOf { BalloonTheme() }