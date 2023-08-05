@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LingshotFullScreenDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onActions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        LingshotLayout(
            title = title,
            icon = Icons.Default.Close,
            onClickNavigation = onDismiss,
            actions = onActions
        ) {
            content()
        }
    }
    LingshotFullScreenDialogStatusBarColor()
}

@Composable
private fun LingshotFullScreenDialogStatusBarColor(
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
