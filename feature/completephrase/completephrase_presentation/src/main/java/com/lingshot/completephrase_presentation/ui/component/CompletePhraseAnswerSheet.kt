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

package com.lingshot.completephrase_presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.completephrase_presentation.R
import com.lingshot.designsystem.theme.LocalSchemeCustom

@Composable
internal fun CompletePhraseAnswerSheet(
    answerState: AnswerState,
    correctAnswer: String,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val state = rememberModalBottomSheetState(
        confirmValueChange = { false },
    )

    ModalBottomSheet(
        modifier = modifier,
        containerColor = answerState.containerColor,
        scrimColor = Color.Transparent,
        shape = RoundedCornerShape(0.dp),
        sheetState = state,
        dragHandle = null,
        onDismissRequest = onDismiss,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = answerState.icon,
                tint = answerState.itemColor,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = answerState.phrase,
                    color = answerState.itemColor,
                )
                if (!answerState.isSuccess) {
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                stringResource(
                                    R.string.text_label_sheet_correct_answer_complete_phrase,
                                ),
                            )
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                            append(correctAnswer)
                        }
                    }
                    Text(
                        text = text,
                        color = answerState.itemColor,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            FilledTonalButton(
                colors = answerState.buttonColor,
                onClick = onContinue,
            ) {
                Text(stringResource(R.string.text_button_continue_answer_sheet_complete_phrase))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseAnswerSheetPreview() {
    CompletePhraseAnswerSheet(
        answerState = AnswerState(),
        correctAnswer = "go",
        onContinue = {},
        onDismiss = {},
    )
}

@Stable
data class AnswerState(
    val isSuccess: Boolean = true,
) {
    private val localSchemeCustom
        @Composable get() = LocalSchemeCustom.current

    val icon: ImageVector = if (isSuccess) {
        Icons.Default.Verified
    } else {
        Icons.Default.Error
    }

    val phrase: String
        @Composable get() {
            val phrasesArray = if (isSuccess) {
                R.array.answer_success_phrases
            } else {
                R.array.answer_error_phrases
            }
            return stringArrayResource(phrasesArray).random()
        }

    val itemColor: Color
        @Composable get() = if (isSuccess) {
            localSchemeCustom.onAnswerSuccessContainer
        } else {
            localSchemeCustom.onAnswerErrorContainer
        }

    val containerColor: Color
        @Composable get() = if (isSuccess) {
            localSchemeCustom.answerSuccessContainer
        } else {
            localSchemeCustom.answerErrorContainer
        }

    val buttonColor: ButtonColors
        @Composable get() =
            ButtonDefaults.buttonColors(
                containerColor =
                if (isSuccess) {
                    localSchemeCustom.answerSuccess
                } else {
                    localSchemeCustom.answerError
                },
                contentColor = if (isSuccess) {
                    localSchemeCustom.onAnswerSuccess
                } else {
                    localSchemeCustom.onAnswerError
                },
            )
}
