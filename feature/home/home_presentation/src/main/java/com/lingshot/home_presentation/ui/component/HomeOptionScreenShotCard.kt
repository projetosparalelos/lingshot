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

package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.home_domain.R.*
import com.lingshot.home_presentation.R

@Composable
internal fun HomeOptionScreenShotCard(
    icon: ImageVector,
    imageOnboarding: Int,
    title: Int,
    description: Int,
    isEnabled: Boolean,
    isChecked: Boolean,
    onClickChanged: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier.clickable(enabled = isEnabled, onClick = onClickChanged)) {
        ListItem(
            headlineContent = { Text(stringResource(id = title), fontWeight = FontWeight.Bold) },
            trailingContent = {
                Switch(
                    enabled = isEnabled,
                    checked = isChecked,
                    onCheckedChange = { onClickChanged() },
                )
            },
            leadingContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            },
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(id = description),
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(id = imageOnboarding),
                contentDescription = null,
            )
            Spacer(modifier = Modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeOptionScreenShotCardPreview() {
    HomeOptionScreenShotCard(
        icon = Icons.Default.Screenshot,
        imageOnboarding = R.drawable.floating_balloon_onboarding,
        title = string.text_title_by_floating_balloon_home,
        description = string.text_description_floating_balloon_home,
        isEnabled = true,
        isChecked = false,
        onClickChanged = {},
    )
}
