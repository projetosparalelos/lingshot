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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.phrasemaster_presentation.ui

import android.widget.Toast.LENGTH_LONG
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.designsystem.component.LingshotFullScreenDialog
import com.lingshot.designsystem.component.LingshotFullScreenDialogStatusBarColor
import com.lingshot.phrasemaster_presentation.R
import com.phrase.phrasemaster_domain.model.PhraseDomain
import dev.jeziellago.compose.markdowntext.MarkdownText
import es.dmoral.toasty.Toasty.warning

@Composable
fun EditPhraseFullScreenDialog(
    phraseState: PhraseState,
    onSavePhraseInLanguageCollection: (PhraseDomain) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var state by remember { mutableStateOf(phraseState) }

    LingshotFullScreenDialog(
        modifier = modifier,
        title = stringResource(R.string.text_title_toolbar_edit_phrase),
        onDismiss = onDismiss,
        onActions = {
            TextButton(onClick = {
                state.updateParenthesesStatus()

                if (state.hasWordInDoubleParentheses) {
                    onSavePhraseInLanguageCollection(state.phraseDomain)
                }
            }) {
                Text(text = stringResource(R.string.text_button_save_edit_phrase))
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phraseDomain.original,
                onValueChange = { newValue ->
                    state = state.copy(
                        phraseDomain = state.phraseDomain.copy(original = newValue),
                    )

                    state.updateParenthesesStatus()
                },
                label = {
                    Text(
                        text = stringResource(R.string.text_label_original_input_edit_phrase),
                    )
                },
            )
            MarkdownText(
                color = MaterialTheme.colorScheme.onSurface,
                markdown = stringResource(R.string.text_markdown_enclose_word_edit_phrase),
            )
            AnimatedVisibility(visible = state.hasWordInDoubleParentheses.not()) {
                MarkdownText(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                    markdown = stringResource(R.string.text_markdown_alert_enclose_word_edit_phrase),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phraseDomain.translate,
                onValueChange = { newValue ->
                    state = state.copy(
                        phraseDomain = state.phraseDomain.copy(translate = newValue),
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.text_label_translate_input_edit_phrase),
                    )
                },
            )
        }
    }

    LingshotFullScreenDialogStatusBarColor()

    if (phraseState.isValidLanguage.not()) {
        warning(
            context,
            stringResource(R.string.text_message_invalid_language),
            LENGTH_LONG,
            true,
        ).show()
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPhraseFullScreenDialogPreview() {
    EditPhraseFullScreenDialog(
        onDismiss = {},
        phraseState = PhraseState(
            PhraseDomain(
                "What's your name?",
                "Qual seu nome?",
            ),
        ),
        onSavePhraseInLanguageCollection = {},
    )
}

@Immutable
data class PhraseState(
    val phraseDomain: PhraseDomain = PhraseDomain(),
    val isValidLanguage: Boolean = true,
) {
    var hasWordInDoubleParentheses by mutableStateOf(true)
        private set

    fun updateParenthesesStatus() {
        val regex = Regex("\\(\\(([^()\\s]+)\\)\\)")
        val matches = regex.findAll(phraseDomain.original)
        val words = matches.map { it.groupValues[1] }.toList()

        hasWordInDoubleParentheses = (words.size == 1)
    }
}
