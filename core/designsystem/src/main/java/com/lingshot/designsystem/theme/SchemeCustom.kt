package com.lingshot.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class SchemeCustom(
    val goalsPieChart: Color = Color.Unspecified,
    val completedPieChart: Color = Color.Unspecified,
    val answerSuccess: Color = Color.Unspecified,
    val onAnswerSuccess: Color = Color.Unspecified,
    val answerSuccessContainer: Color = Color.Unspecified,
    val onAnswerSuccessContainer: Color = Color.Unspecified,
    val answerError: Color = Color.Unspecified,
    val onAnswerError: Color = Color.Unspecified,
    val answerErrorContainer: Color = Color.Unspecified,
    val onAnswerErrorContainer: Color = Color.Unspecified,
    val overlay: Color = background_overlay
)

val LocalSchemeCustom = staticCompositionLocalOf { SchemeCustom() }
