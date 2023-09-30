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
                    consecutiveDays,
                ),
            )
        }
    }
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeOffensiveTitlePreview() {
    LingshotTheme {
        HomeOffensiveTitle(consecutiveDays = 5)
    }
}
