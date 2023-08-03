@file:OptIn(ExperimentalLayoutApi::class)

package com.lingshot.phrasemaster_presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.designsystem.component.LingshotMeasureUnconstrained
import com.lingshot.domain.helper.FormatPhraseHelper.extractWordsInDoubleParentheses
import com.lingshot.domain.helper.FormatPhraseHelper.removeDoubleParenthesesAllPhrase

@Composable
fun CompleteSentenceScreen() {
    val sentence = "How are ((you)) doing?"
    val sentenceWithoutParentheses = removeDoubleParenthesesAllPhrase(sentence)
    val getWordWithoutParentheses = extractWordsInDoubleParentheses(sentence)
    val parts = sentenceWithoutParentheses.split(getWordWithoutParentheses)

    var userInput by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            FlowRow(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textTypography = MaterialTheme.typography.headlineSmall
                val textColor = MaterialTheme.colorScheme.primary

                parts.forEachIndexed { index, part ->
                    Text(
                        text = part,
                        style = textTypography,
                        color = textColor
                    )
                    if (index < parts.size - 1) {
                        val isError = userInput.text.isNotEmpty() && !userInput.text.startsWith(
                            getWordWithoutParentheses.substring(0, userInput.text.length)
                        )

                        val colorBasicTextField = if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            textColor.copy(alpha = 0.8f)
                        }

                        LingshotMeasureUnconstrained(viewToMeasure = {
                            Text(
                                text = getWordWithoutParentheses,
                                style = textTypography,
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }) { measuredWidth, measuredHeight ->
                            BasicTextField(
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(4.dp)
                                    .size(width = measuredWidth, height = measuredHeight)
                                    .padding(horizontal = 4.dp),
                                value = userInput,
                                singleLine = true,
                                textStyle = textTypography.copy(color = colorBasicTextField),
                                cursorBrush = SolidColor(colorBasicTextField),
                                onValueChange = {
                                    if (it.text.length <= getWordWithoutParentheses.length) {
                                        userInput = it
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(text = "Como você está?", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteSentenceScreenPreview() {
    CompleteSentenceScreen()
}
