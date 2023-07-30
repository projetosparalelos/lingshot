@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.phrasemaster_presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.designsystem.component.LingshotFullScreenPopup
import com.lingshot.phrasemaster_presentation.R
import com.phrase.phrasemaster_domain.model.PhraseDomain
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun EditPhraseFullScreenPopup(
    phrase: PhraseDomain,
    onPhraseChange: (PhraseDomain) -> Unit,
    onSavePhraseInLanguageCollection: (PhraseDomain) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val id by rememberSaveable { mutableStateOf(phrase.id) }
    var hasSingleWordInDoubleSquareBrackets by remember { mutableStateOf(true) }

    LingshotFullScreenPopup(
        modifier = modifier,
        title = stringResource(R.string.text_title_toolbar_edit_phrase),
        onDismiss = onDismiss,
        onActions = {
            TextButton(onClick = {
                hasSingleWordInDoubleSquareBrackets =
                    hasSingleWordInDoubleSquareBrackets(phrase.original)

                if (hasSingleWordInDoubleSquareBrackets) {
                    onSavePhraseInLanguageCollection(phrase.copy(id = id))
                }
            }) {
                Text(text = stringResource(R.string.text_button_save_edit_phrase))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phrase.original,
                onValueChange = { newValue ->
                    onPhraseChange(phrase.copy(original = newValue))
                    hasSingleWordInDoubleSquareBrackets =
                        hasSingleWordInDoubleSquareBrackets(newValue)
                },
                label = {
                    Text(
                        text = stringResource(R.string.text_label_original_input_edit_phrase)
                    )
                }
            )
            MarkdownText(
                markdown = stringResource(R.string.text_markdown_enclose_word_edit_phrase)
            )
            AnimatedVisibility(visible = hasSingleWordInDoubleSquareBrackets.not()) {
                MarkdownText(
                    modifier = Modifier.padding(top = 4.dp),
                    markdown = stringResource(R.string.text_markdown_alert_enclose_word_edit_phrase)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phrase.translate,
                onValueChange = { newValue ->
                    onPhraseChange(phrase.copy(translate = newValue))
                },
                label = {
                    Text(
                        text = stringResource(R.string.text_label_translate_input_edit_phrase)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPhraseFullScreenPopupPreview() {
    EditPhraseFullScreenPopup(
        onDismiss = {},
        phrase = PhraseDomain(
            "What's your name?",
            "Qual seu nome?"
        ),
        onPhraseChange = {},
        onSavePhraseInLanguageCollection = {}
    )
}

private fun hasSingleWordInDoubleSquareBrackets(phrase: String): Boolean {
    val regex = Regex("\\[\\[([^\\[\\]\\s]+)]]")
    val matches = regex.findAll(phrase)
    val words = matches.map { it.groupValues[1] }.toList()

    return words.size == 1
}
