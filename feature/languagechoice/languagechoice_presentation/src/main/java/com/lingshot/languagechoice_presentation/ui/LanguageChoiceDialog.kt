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

package com.lingshot.languagechoice_presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.lingshot.languagechoice_presentation.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun LanguageChoiceDialog(
    availableLanguage: AvailableLanguage?,
    availableLanguageList: ImmutableList<AvailableLanguage>,
    onSaveLanguage: (AvailableLanguage?) -> Unit,
    onSelectedOptionsLanguage: (AvailableLanguage?) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier.height(500.dp),
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.text_button_dialog_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSaveLanguage(availableLanguage)
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.text_button_select_dialog_choose_language))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.text_title_dialog_choose_language),
                fontSize = 18.sp,
            )
        },
        text = {
            LanguageChoiceList(
                availableLanguage = availableLanguage,
                availableLanguageList = availableLanguageList,
                onSelectedOptionsLanguage = onSelectedOptionsLanguage,
            )
        },
    )
}

@Composable
private fun LanguageChoiceList(
    availableLanguage: AvailableLanguage?,
    availableLanguageList: ImmutableList<AvailableLanguage>,
    onSelectedOptionsLanguage: (AvailableLanguage?) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(availableLanguageList, key = { it.languageCode }) { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == availableLanguage),
                        onClick = {
                            onSelectedOptionsLanguage(language)
                        },
                    )
                    .background(
                        if (language == availableLanguage) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LanguageChoiceEmojiFlag(emojiFlag = language.flagEmoji)
                Text(
                    text = stringResource(id = language.displayName),
                    modifier = Modifier.padding(start = 14.dp),
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        availableLanguage?.let {
            val index = availableLanguageList.indexOf(it)
            if (index >= 0) {
                lazyListState.scrollToItem(index)
            }
        }
    }
}

@Composable
private fun LanguageChoiceEmojiFlag(
    emojiFlag: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = emojiFlag,
        fontSize = 32.sp,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LanguageChoiceEmojiFlagPreview() {
    LanguageChoiceDialog(
        availableLanguage = AvailableLanguage.ENGLISH,
        availableLanguageList = enumValues<AvailableLanguage>().toList().toImmutableList(),
        onSaveLanguage = {},
        onSelectedOptionsLanguage = {},
        onDismiss = {},
    )
}
