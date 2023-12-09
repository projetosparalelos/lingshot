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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.subtitle_presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.subtitle_presentation.R

@Composable
internal fun SubtitleNavigationItem(
    countPhraseSelected: Int,
    onFetchTextRecognizerSelect: () -> Unit,
    onFetchTextTranslate: () -> Unit,
    onFocusImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButtonWithText(
            text = stringResource(id = R.string.text_label_navigation_item_translate),
            onClick = onFetchTextTranslate,
            icon = {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                )
            },
        )

        BadgedBox(badge = { Badge(modifier = Modifier.padding(top = 18.dp)) { Text(countPhraseSelected.toString()) } }) {
            IconButtonWithText(
                text = stringResource(id = R.string.text_label_navigation_item_select),
                onClick = onFetchTextRecognizerSelect,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                    )
                },
            )
        }

        IconButtonWithText(
            text = stringResource(id = R.string.text_label_navigation_item_focus),
            onClick = onFocusImage,
            icon = {
                Icon(
                    imageVector = Icons.Default.ZoomOutMap,
                    contentDescription = null,
                )
            },
        )
    }
}

@Composable
private fun IconButtonWithText(
    text: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(modifier = modifier, onClick = onClick) {
            icon()
        }
        Text(text = text, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun SubtitleNavigationItemPreview() {
    SubtitleNavigationItem(
        countPhraseSelected = 1,
        onFetchTextRecognizerSelect = {},
        onFetchTextTranslate = {},
        onFocusImage = {},
    )
}
