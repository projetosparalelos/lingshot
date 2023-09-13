package com.lingshot.completephrase_presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun CompletePhraseIndicatorPage(
    currentPage: Int,
    totalPage: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentPage.toFloat() / totalPage

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "$currentPage/$totalPage",
            style = MaterialTheme.typography.bodyMedium
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseIndicatorPagePreview() {
    CompletePhraseIndicatorPage(
        currentPage = 0,
        totalPage = 10
    )
}
