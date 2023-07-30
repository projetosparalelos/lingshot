@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LingshotFullScreenPopup(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onActions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            excludeFromSystemGesture = true
        )
    ) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            LingshotFullScreenPopupStatusBarColor()
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = title)
                    },
                    actions = onActions,
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                )
                content()
            }
        }
    }
}

@Composable
private fun LingshotFullScreenPopupStatusBarColor(
    color: Color = MaterialTheme.colorScheme.surface,
    systemUiController: SystemUiController = rememberSystemUiController()
) {
    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(color)

        onDispose {
            systemUiController.setStatusBarColor(Color.Transparent)
        }
    }
}
