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

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder
import com.lingshot.domain.model.UserDomain
import com.lingshot.home_presentation.R

@Composable
internal fun HomeToolbar(
    userDomain: UserDomain?,
    isExpandedDropdownMenuSignOut: Boolean,
    onToggleExpandDropdownMenuSignOut: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .placeholder(
                        visible = userDomain == null,
                        highlight = PlaceholderHighlight.fade(),
                    ),
                text = String.format(
                    stringResource(
                        R.string.text_label_name_user_logged_home,
                        userDomain?.firstName.toString(),
                    ),
                ),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onToggleExpandDropdownMenuSignOut,
                enabled = userDomain != null,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .placeholder(
                            visible = userDomain == null,
                            highlight = PlaceholderHighlight.fade(),
                        )
                        .clip(CircleShape),
                    model = userDomain?.profilePictureUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                DropdownMenu(
                    expanded = isExpandedDropdownMenuSignOut,
                    onDismissRequest = onToggleExpandDropdownMenuSignOut,
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                        },
                        text = {
                            Text(
                                stringResource(id = R.string.text_label_dropdown_menu_sign_out_home),
                            )
                        },
                        onClick = onSignOut,
                    )
                }
            }
        },
        actions = {
            IconButton(enabled = false, onClick = {}) {
                Icon(Icons.Rounded.Settings, contentDescription = null)
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeToolbarPreview() {
    HomeToolbar(
        userDomain = UserDomain(),
        isExpandedDropdownMenuSignOut = false,
        onToggleExpandDropdownMenuSignOut = {},
        onSignOut = {},
    )
}
