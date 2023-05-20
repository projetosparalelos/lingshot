package com.teachmeprint.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teachmeprint.designsystem.R

@Composable
fun TeachMePrintSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    textButton: String = stringResource(id = R.string.text_button_action_close_snack_bar),
    onClick: (() -> Unit)? = null
) {
    var isVisibleState by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisibleState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Snackbar(modifier = modifier,
            dismissAction = {
                TextButton(
                    onClick = {
                        onClick?.invoke()
                        isVisibleState = !isVisibleState
                    }) {
                    Text(
                        text = textButton,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }) {
            Text(text = message)
        }
    }
}