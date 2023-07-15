package com.lingshot.designsystem.component

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
import com.lingshot.designsystem.R

@Composable
fun LingshotSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    textButton: String = stringResource(id = R.string.text_button_action_close_snack_bar),
    onDismiss: (() -> Unit)? = null
) {
    var isVisibleState by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisibleState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Snackbar(
            modifier = modifier,
            dismissAction = {
                TextButton(
                    onClick = {
                        onDismiss?.invoke()
                        isVisibleState = !isVisibleState
                    }
                ) {
                    Text(
                        text = textButton,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        ) {
            Text(text = message)
        }
    }
}
