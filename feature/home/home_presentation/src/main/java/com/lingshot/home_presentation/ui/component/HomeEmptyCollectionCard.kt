package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun HomeEmptyCollectionCard(modifier: Modifier = Modifier) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        ) {
            append("(  * _ *  )")
        }
        append("\n")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
        ) {
            append("Collection is empty. Add a phrase!")
        }
    }
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0f)
                    )
                )
            ),
        lineHeight = 32.sp,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeEmptyCollectionCardPreview(modifier: Modifier = Modifier) {
    HomeEmptyCollectionCard()
}
