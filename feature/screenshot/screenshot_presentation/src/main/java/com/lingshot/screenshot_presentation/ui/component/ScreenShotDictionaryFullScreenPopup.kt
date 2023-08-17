package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.component.LingshotFullScreenDialog
import com.lingshot.designsystem.component.LingshotWebView
import com.lingshot.screenshot_presentation.R

@Composable
fun ScreenShotDictionaryFullScreenDialog(
    url: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    LingshotFullScreenDialog(
        modifier = modifier,
        title = stringResource(R.string.text_title_dictionary_full_screen_dialog_screenshot),
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
