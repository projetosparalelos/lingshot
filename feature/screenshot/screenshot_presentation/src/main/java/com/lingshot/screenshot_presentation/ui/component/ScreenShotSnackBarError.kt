package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.component.LingshotSnackBar

@Composable
fun ScreenShotSnackBarError(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    LingshotSnackBar(
        modifier = modifier,
        message = message,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarErrorPreview() {
    ScreenShotSnackBarError("Error message.") {}
}
