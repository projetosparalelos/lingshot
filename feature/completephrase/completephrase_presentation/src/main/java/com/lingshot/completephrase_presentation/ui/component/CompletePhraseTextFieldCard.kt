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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.completephrase_presentation.ReviewLevel
import com.lingshot.designsystem.component.LingshotMeasureUnconstrained
import com.lingshot.designsystem.component.LingshotPulseAnimation
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder
import com.lingshot.domain.helper.FormatPhraseHelper

@Composable
fun CompletePhraseTextFieldCard(enableVoice: Boolean, modifier: Modifier = Modifier) {
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
                CompletePhrasePlayAudioButton(enableVoice)
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(enableVoice, highlight = PlaceholderHighlight.fade()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CompletePhraseRenderTextWithField(enableVoice)
            }
            Spacer(modifier = Modifier.height(24.dp))
            CompletePhraseShowWordButton()
        }
    }
}

@Composable
private fun CompletePhraseRenderTextWithField(enableVoice: Boolean) {
    val sentence = "Let's ((go))! It's time to learn!"

    val words = FormatPhraseHelper.processPhraseWithDoubleParentheses(sentence)
    val getWordWithoutParentheses = FormatPhraseHelper.extractWordsInDoubleParentheses(sentence)

    var userInput by remember { mutableStateOf(TextFieldValue()) }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val textTypography = MaterialTheme.typography.headlineSmall
    val textColor = MaterialTheme.colorScheme.primary

    words.forEach { word ->
        if (word == "(($getWordWithoutParentheses))") {
            val isError = userInput.text.isNotEmpty() && !userInput.text.startsWith(
                getWordWithoutParentheses.substring(0, userInput.text.length),
                ignoreCase = true
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

                            if (userInput.text == getWordWithoutParentheses) {
                                softwareKeyboardController?.hide()
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
private fun ColumnScope.CompletePhraseShowWordButton() {
    ElevatedButton(
        modifier = Modifier.align(Alignment.End),
        onClick = { /*TODO*/ }
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
private fun CompletePhrasePlayAudioButton(enableVoice: Boolean) {
    LingshotPulseAnimation(enableAnimation = enableVoice) {
        IconButton(
            onClick = { /*TODO*/ }
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
    CompletePhraseTextFieldCard(false)
}
