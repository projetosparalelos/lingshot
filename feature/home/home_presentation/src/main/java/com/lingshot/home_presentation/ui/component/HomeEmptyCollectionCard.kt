/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lingshot.home_presentation.R

@Composable
internal fun HomeEmptyCollectionCard(modifier: Modifier = Modifier) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            ),
        ) {
            append(stringResource(R.string.text_emoji_empty_collection_card_home))
        }
        append("\n")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
            ),
        ) {
            append(stringResource(R.string.text_message_empty_collection_card_home))
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
                        Color.Black.copy(alpha = 0f),
                    ),
                ),
            ),
        lineHeight = 32.sp,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeEmptyCollectionCardPreview() {
    HomeEmptyCollectionCard()
}
