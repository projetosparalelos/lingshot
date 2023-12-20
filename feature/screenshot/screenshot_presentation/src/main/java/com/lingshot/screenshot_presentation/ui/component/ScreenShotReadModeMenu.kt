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
package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.screenshot_domain.model.ReadModeType
import com.lingshot.screenshot_domain.model.ReadModeType.SPEECH_BUBBLE
import com.lingshot.screenshot_domain.model.ReadModeType.STANDARD
import com.lingshot.screenshot_presentation.R

@Composable
internal fun ScreenShotReadModeMenu(
    readModeType: ReadModeType?,
    onChangeReadMode: (ReadModeType) -> Unit,
    modifier: Modifier = Modifier,
    onClear: () -> Unit,
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expandedMenu = true },
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false },
        ) {
            DropdownMenuItem(
                enabled = readModeType != STANDARD,
                text = {
                    Text(stringResource(R.string.text_menu_item_standard_read_mode_home))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Cookie,
                        contentDescription = null,
                    )
                },
                onClick = {
                    onChangeReadMode(STANDARD)
                },
            )
            DropdownMenuItem(
                enabled = readModeType != SPEECH_BUBBLE,
                text = {
                    Text(stringResource(R.string.text_menu_item_speech_bubble_read_mode_home))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                    )
                },
                onClick = {
                    onChangeReadMode(SPEECH_BUBBLE)
                },
            )
        }
    }

    LaunchedEffect(expandedMenu) {
        if (expandedMenu) {
            onClear()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotReadModeMenuPreview() {
    ScreenShotReadModeMenu(
        readModeType = STANDARD,
        onChangeReadMode = {},
        onClear = {},
    )
}
