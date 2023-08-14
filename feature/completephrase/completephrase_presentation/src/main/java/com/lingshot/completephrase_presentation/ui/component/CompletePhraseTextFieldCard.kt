@file:OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)

package com.lingshot.completephrase_presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.completephrase_presentation.ReviewLevel
import com.lingshot.designsystem.component.LingshotMeasureUnconstrained
import com.lingshot.designsystem.component.LingshotPulseAnimation
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CompletePhraseTextFieldCard(
    listWords: ImmutableList<String>,
    wordWithoutParentheses: String,
    wordToFill: String,
    onFillWord: (String) -> Unit,
    isSpeechActive: Boolean,
    onSpeakText: () -> Unit,
    modifier: Modifier = Modifier
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    var isShimmerVisible by remember { mutableStateOf(true) }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                CompletePhraseReviewLevel()
                Spacer(modifier = Modifier.weight(1f))
                CompletePhrasePlayAudioButton(
                    isSpeechActive = isSpeechActive,
                    onSpeakText = {
                        onSpeakText()
                        isShimmerVisible = false
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(
                        visible = isSpeechActive && isShimmerVisible,
                        highlight = PlaceholderHighlight.fade()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CompletePhraseRenderTextWithField(
                    listWords = listWords,
                    wordWithoutParentheses = wordWithoutParentheses,
                    wordToFill = wordToFill,
                    onFillWord = onFillWord,
                    onHideKeyboard = {
                        softwareKeyboardController?.hide()
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            CompletePhraseShowWordButton(
                onFillWord = {
                    onFillWord(wordWithoutParentheses)
                    softwareKeyboardController?.hide()
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        onSpeakText()
    }
}

@Composable
private fun CompletePhraseRenderTextWithField(
    listWords: ImmutableList<String>,
    wordWithoutParentheses: String,
    wordToFill: String,
    onFillWord: (String) -> Unit,
    onHideKeyboard: () -> Unit
) {
    val textTypography = MaterialTheme.typography.headlineSmall
    val textColor = MaterialTheme.colorScheme.primary

    listWords.forEach { word ->
        if (word == "(($wordWithoutParentheses))") {
            val isError = wordToFill.isNotEmpty() && !wordToFill.startsWith(
                wordWithoutParentheses.substring(0, wordToFill.length),
                ignoreCase = true
            )

            val colorBasicTextField = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                textColor.copy(alpha = 0.8f)
            }
            LingshotMeasureUnconstrained(viewToMeasure = {
                Text(
                    text = wordWithoutParentheses,
                    style = textTypography,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }) { measuredWidth, measuredHeight ->
                BasicTextField(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                        .size(width = measuredWidth, height = measuredHeight)
                        .padding(horizontal = 4.dp),
                    value = wordToFill,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true
                    ),
                    textStyle = textTypography.copy(color = colorBasicTextField),
                    cursorBrush = SolidColor(colorBasicTextField),
                    onValueChange = {
                        if (it.length <= wordWithoutParentheses.length) {
                            onFillWord(it)

                            if (it.equals(wordWithoutParentheses, ignoreCase = true)) {
                                onHideKeyboard()
                            }
                        }
                    }
                )
            }
        } else {
            Text(
                text = word,
                style = textTypography,
                color = textColor
            )
        }
    }
}

@Composable
private fun ColumnScope.CompletePhraseShowWordButton(onFillWord: () -> Unit) {
    ElevatedButton(
        modifier = Modifier.align(Alignment.End),
        onClick = onFillWord
    ) {
        Text(
            text = "Show word"
        )
    }
}

@Composable
private fun CompletePhraseReviewLevel() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(ReviewLevel.values().size) { index ->
            val icon =
                if (index < 2) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline

            Icon(
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.secondary,
                imageVector = icon,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = ReviewLevel.from(2)?.label.toString(),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun CompletePhrasePlayAudioButton(isSpeechActive: Boolean, onSpeakText: () -> Unit) {
    LingshotPulseAnimation(enableAnimation = isSpeechActive) {
        IconButton(
            onClick = onSpeakText
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseTextFieldCardPreview() {
    CompletePhraseTextFieldCard(
        listWords = emptyList<String>().toImmutableList(),
        wordWithoutParentheses = "go",
        wordToFill = "",
        onFillWord = {},
        isSpeechActive = false,
        onSpeakText = {}
    )
}
