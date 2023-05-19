package com.teachmeprint.language.screen_shot.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teachmeprint.language.R
import com.teachmeprint.language.core.designsystem.component.TeachMePrintSnackBar

@Composable
fun ScreenShotSnackBarSelectLanguage(
    modifier: Modifier = Modifier,
    onToggleLanguageDialogAndHideSelectionAlert: () -> Unit
) {
    TeachMePrintSnackBar(
        modifier = modifier,
        message = stringResource(id = R.string.text_select_language_speak_message),
        textButton = stringResource(id = R.string.text_button_action_select_snack_bar),
        onClick = onToggleLanguageDialogAndHideSelectionAlert
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarSelectLanguagePreview() {
    ScreenShotSnackBarSelectLanguage(
        onToggleLanguageDialogAndHideSelectionAlert = {}
    )
}