package com.lingshot.home_presentation.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.home_presentation.R

@Composable
internal fun HomeOffensiveTitle(consecutiveDays: Int, modifier: Modifier = Modifier) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.text_label_your_offence_home))
            append(" ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
            append(
                String.format(
                    stringResource(id = R.string.text_label_consecutive_days_home),
                    consecutiveDays
                )
            )
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
        HomeOffensiveTitle(consecutiveDays = 5)
    }
}
