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
package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lingshot.languagechoice_domain.model.AvailableLanguage

@Composable
internal fun HomeLanguageChoice(
    languageFrom: AvailableLanguage?,
    languageTo: AvailableLanguage?,
    onClickLanguageFrom: () -> Unit,
    onClickLanguageTo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        FilledTonalButton(
            onClick = onClickLanguageFrom,
        ) {
            Text("${languageFrom?.flagEmoji}  ${languageFrom?.let { stringResource(id = it.displayName) }}")
        }

        Icon(
            imageVector = Icons.Default.CompareArrows,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )

        FilledTonalButton(
            onClick = onClickLanguageTo,
        ) {
            Text("${languageTo?.flagEmoji}  ${languageTo?.let { stringResource(id = it.displayName) }}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLanguageChoicePreview() {
    HomeLanguageChoice(
        languageFrom = AvailableLanguage.ENGLISH,
        languageTo = AvailableLanguage.PORTUGUESE,
        onClickLanguageFrom = {},
        onClickLanguageTo = {},
    )
}
