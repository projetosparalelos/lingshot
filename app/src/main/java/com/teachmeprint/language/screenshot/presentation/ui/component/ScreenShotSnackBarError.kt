package com.teachmeprint.language.screenshot.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teachmeprint.language.core.designsystem.component.TeachMePrintSnackBar
import com.teachmeprint.language.core.common.helper.StatusMessage.getErrorMessage

@Composable
fun ScreenShotSnackBarError(modifier: Modifier = Modifier, code: Int) {
    TeachMePrintSnackBar(
        modifier = modifier,
        message = stringResource(id = getErrorMessage(code))
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarErrorPreview() {
    ScreenShotSnackBarError(code = 0)
}