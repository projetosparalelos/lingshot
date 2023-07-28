@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.screenshot_presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lingshot.domain.model.PhraseDomain
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun EditPhraseFullScreenPopup(
    modifier: Modifier = Modifier,
    phrase: PhraseDomain,
    onPhraseChange: (PhraseDomain) -> Unit,
    onDismiss: () -> Unit
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            excludeFromSystemGesture = true
        )
    ) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Edit sentence")
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    },
                    actions = {
                        TextButton(onClick = {}) {
                            Text(text = "Save")
                        }
                    }
                )
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
                        },
                        label = { Text(text = "Original") }
                    )
                    MarkdownText(
                        markdown = "Enclose a word you want to practice in `{{ }}`, " +
                            "for example: `What's your {{name}}?`"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = phrase.translate,
                        onValueChange = { newValue ->
                            onPhraseChange(phrase.copy(translate = newValue))
                        },
                        label = { Text(text = "Translation") }
                    )
                }
            }
        }
    }
}

fun hasSingleWordInDoubleBrackets(phrase: String): Boolean {
    val regex = Regex("\\{\\{([^{}\\s]+)}}")
    val matches = regex.findAll(phrase)
    val words = matches.map { it.groupValues[1] }.toList()

    return words.size == 1
}

@Preview(showBackground = true)
@Composable
private fun EditPhraseFullScreenPopupPreview() {
    EditPhraseFullScreenPopup(
        onDismiss = {},
        phrase = PhraseDomain("What's your name?", "Qual seu nome?"),
        onPhraseChange = {}
    )
}
