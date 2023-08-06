@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun LingshotLayout(
    title: String,
    onClickNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.ArrowBack,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                actions = actions,
                navigationIcon = {
                    IconButton(onClick = onClickNavigation) {
                        Icon(icon, contentDescription = null)
                    }
                }
            )
            content()
        }
    }
}
