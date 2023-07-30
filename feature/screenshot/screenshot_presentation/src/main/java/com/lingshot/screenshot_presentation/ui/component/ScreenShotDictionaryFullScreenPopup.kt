package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.component.LingshotFullScreenPopup
import com.lingshot.designsystem.component.LingshotWebView

@Composable
fun ScreenShotDictionaryFullScreenPopup(
    url: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    LingshotFullScreenPopup(
        modifier = modifier,
        title = "Dictionary",
        onDismiss = onDismiss
    ) {
        LingshotWebView(url = url)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotDictionaryFullScreenPopupPreview() {
    ScreenShotDictionaryFullScreenPopup(
        url = "http://url",
        onDismiss = {}
    )
}
