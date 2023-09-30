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
import com.lingshot.designsystem.component.LingshotFullScreenDialog
import com.lingshot.designsystem.component.LingshotFullScreenDialogStatusBarColor
import com.lingshot.designsystem.component.LingshotWebView
import com.lingshot.screenshot_presentation.R

@Composable
internal fun ScreenShotDictionaryFullScreenDialog(
    url: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    LingshotFullScreenDialog(
        modifier = modifier,
        title = stringResource(R.string.text_title_dictionary_full_screen_dialog_screenshot),
        onDismiss = onDismiss,
    ) {
        LingshotWebView(url = url)
    }

    LingshotFullScreenDialogStatusBarColor()
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotDictionaryFullScreenDialogPreview() {
    ScreenShotDictionaryFullScreenDialog(
        url = "http://url",
        onDismiss = {},
    )
}
