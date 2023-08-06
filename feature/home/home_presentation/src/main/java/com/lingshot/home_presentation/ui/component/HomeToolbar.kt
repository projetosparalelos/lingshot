@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.lingshot.domain.model.UserDomain

@Composable
fun HomeToolbar(
    userDomain: UserDomain?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Hi, ${userDomain?.firstName}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                AsyncImage(
                    modifier = Modifier.clip(CircleShape),
                    model = userDomain?.profilePictureUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Rounded.Settings, contentDescription = null)
            }
        }
    )
}
