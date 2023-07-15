package com.lingshot.home_presentation.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.theme.LingshotTheme

@Composable
fun HomeOffensiveTitle(modifier: Modifier = Modifier) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Your offence is: ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
            append("5 days")
        }
    }
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeOffensiveTitlePreview() {
    LingshotTheme {
        HomeOffensiveTitle()
    }
}
