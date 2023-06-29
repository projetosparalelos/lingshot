package com.teachmeprint.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teachmeprint.designsystem.component.TeachMePrintSnackBar

@Composable
fun ScreenShotSnackBarError(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    TeachMePrintSnackBar(
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