/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    onDismiss: (() -> Unit)? = null,
) {
    var isVisibleState by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisibleState,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Snackbar(
            modifier = modifier,
            dismissAction = {
                TextButton(
                    onClick = {
                        onDismiss?.invoke()
                        isVisibleState = !isVisibleState
                    },
                ) {
                    Text(
                        text = textButton,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                }
            },
        ) {
            Text(text = message)
        }
    }
}
