package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.component.LingshotFullScreenDialog
import com.lingshot.designsystem.component.LingshotWebView

@Composable
fun ScreenShotDictionaryFullScreenDialog(
    url: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    LingshotFullScreenDialog(
        modifier = modifier,
        title = "Dictionary",
        onDismiss = onDismiss
    ) {
        LingshotWebView(url = url)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotDictionaryFullScreenDialogPreview() {
    ScreenShotDictionaryFullScreenDialog(
        url = "http://url",
        onDismiss = {}
    )
}
