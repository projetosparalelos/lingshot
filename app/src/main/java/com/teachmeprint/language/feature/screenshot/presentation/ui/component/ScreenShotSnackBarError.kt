package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teachmeprint.language.R
import com.teachmeprint.language.core.helper.StatusMessage

@Composable
fun ScreenShotSnackBarError(modifier: Modifier = Modifier, code: Int) {
    var isVisibleState by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisibleState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Snackbar(modifier = modifier,
            dismissAction = {
                TextButton(onClick = {
                    isVisibleState = !isVisibleState
                }) {
                    Text(
                        text = stringResource(id = R.string.text_button_action_close_snack_bar),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }) {
            Text(text = stringResource(id = StatusMessage.getErrorMessage(code)))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarErrorPreview() {
    ScreenShotSnackBarError(code = 0)
}