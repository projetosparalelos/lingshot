package com.teachmeprint.screenshot_presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScreenShotBottomSheet(
    text: String,
    modifier: Modifier = Modifier,
    onHideTranslateBalloon: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onHideTranslateBalloon
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Cancel")
                }
            }, title = { Text(text) }, actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Rounded.Check, contentDescription = "Save")
                    }
                })
        }
    }
}