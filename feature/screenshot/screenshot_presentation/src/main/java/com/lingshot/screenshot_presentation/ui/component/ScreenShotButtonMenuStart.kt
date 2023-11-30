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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.designsystem.theme.md_theme_light_onSurface
import com.lingshot.designsystem.theme.md_theme_light_surface

@Composable
internal fun ScreenShotButtonMenuStart(
    onSelectedOptionsButtonMenuItem: (ButtonMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        modifier = modifier
            .padding(bottom = 16.dp, start = 16.dp)
            .size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = md_theme_light_onSurface,
            contentColor = md_theme_light_surface.copy(alpha = 0.8f),
        ),
        border = BorderStroke(1.dp, md_theme_light_surface.copy(alpha = 0.5f)),
        onClick = { onSelectedOptionsButtonMenuItem(ButtonMenuItem.FOCUS) },
    ) {
        Icon(imageVector = Icons.Default.ZoomOutMap, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotButtonMenuStartPreview() {
    ScreenShotButtonMenuStart(
        onSelectedOptionsButtonMenuItem = {},
    )
}
