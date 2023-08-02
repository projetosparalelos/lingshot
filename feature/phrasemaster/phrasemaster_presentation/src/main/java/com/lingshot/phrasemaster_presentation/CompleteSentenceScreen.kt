@file:OptIn(ExperimentalLayoutApi::class)

package com.lingshot.phrasemaster_presentation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CompleteSentenceScreen() {
    val sentence = "How are you doing? You ((eating)) today?"
    val sentenceWithoutParentheses = sentence.replace(Regex("\\(\\((.*?)\\)\\)"), "$1")
    val getWordWithoutParentheses = extractWordsInDoubleParentheses(sentence)
    val parts = sentenceWithoutParentheses.split(getWordWithoutParentheses)
    var userInput by remember { mutableStateOf(TextFieldValue()) }

    FlowRow(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        parts.forEachIndexed { index, part ->
            Text(
                text = part,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            if (index < parts.size - 1) {
                val isError = userInput.text.isNotEmpty() && !userInput.text.startsWith(
                    getWordWithoutParentheses.substring(0, userInput.text.length)
                )

                MeasureUnconstrained(viewToMeasure = {
                    Text(
                        text = getWordWithoutParentheses,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }) { measuredWidth, measuredHeight ->
                    OutlinedTextField(
                        isError = isError,
                        value = userInput,
                        onValueChange = {
                            userInput = it
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .size(width = measuredWidth, height = measuredHeight)
                    )
                }
            }
        }
    }
}

private fun extractWordsInDoubleParentheses(text: String): String {
    val regex = Regex("\\(\\((.*?)\\)\\)")
    val matches = regex.findAll(text)
    return matches.map { it.groupValues[1] }.joinToString(" ")
}

@Composable
fun MeasureUnconstrained(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp, measuredHeight: Dp) -> Unit
) {
    SubcomposeLayout { constraints ->
        val placeable = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints())

        val measuredWidth = placeable.width.toDp()
        val measuredHeight = placeable.height.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth, measuredHeight)
        }[0].measure(constraints)

        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}

@Preview
@Composable
fun CompleteSentenceScreenPreview() {
    CompleteSentenceScreen()
}
