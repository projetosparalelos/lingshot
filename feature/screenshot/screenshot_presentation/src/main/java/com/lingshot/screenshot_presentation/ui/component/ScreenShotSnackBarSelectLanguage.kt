package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.designsystem.component.LingshotSnackBar
import com.lingshot.screenshot_presentation.R

@Composable
internal fun ScreenShotSnackBarSelectLanguage(
    modifier: Modifier = Modifier,
    onToggleLanguageDialogAndHideSelectionAlert: () -> Unit
) {
    LingshotSnackBar(
        modifier = modifier,
        message = stringResource(id = R.string.text_select_language_speak_message),
        textButton = stringResource(id = R.string.text_button_action_select_snack_bar),
        onDismiss = onToggleLanguageDialogAndHideSelectionAlert
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarSelectLanguagePreview() {
    ScreenShotSnackBarSelectLanguage(
        onToggleLanguageDialogAndHideSelectionAlert = {}
    )
}
