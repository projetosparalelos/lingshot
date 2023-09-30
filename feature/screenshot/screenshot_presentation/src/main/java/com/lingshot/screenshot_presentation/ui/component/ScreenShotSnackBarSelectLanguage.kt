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
    onToggleLanguageDialogAndHideSelectionAlert: () -> Unit,
) {
    LingshotSnackBar(
        modifier = modifier,
        message = stringResource(id = R.string.text_select_language_speak_message),
        textButton = stringResource(id = R.string.text_button_action_select_snack_bar),
        onDismiss = onToggleLanguageDialogAndHideSelectionAlert,
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotSnackBarSelectLanguagePreview() {
    ScreenShotSnackBarSelectLanguage(
        onToggleLanguageDialogAndHideSelectionAlert = {},
    )
}
