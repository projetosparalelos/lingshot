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
package com.lingshot.completephrase_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.common.helper.onSuccess
import com.lingshot.completephrase_presentation.CompletePhraseEvent
import com.lingshot.completephrase_presentation.CompletePhraseEvent.ClearState
import com.lingshot.completephrase_presentation.CompletePhraseEvent.FetchAnswerSound
import com.lingshot.completephrase_presentation.CompletePhraseEvent.FetchAnswersFinished
import com.lingshot.completephrase_presentation.CompletePhraseEvent.FetchTextToSpeech
import com.lingshot.completephrase_presentation.CompletePhraseEvent.FillWord
import com.lingshot.completephrase_presentation.CompletePhraseEvent.HideAnswerSheet
import com.lingshot.completephrase_presentation.CompletePhraseEvent.ShowAnswerSheet
import com.lingshot.completephrase_presentation.CompletePhraseEvent.ToggleTranslatedTextVisibility
import com.lingshot.completephrase_presentation.CompletePhraseEvent.UpdatePhrasePositionOnError
import com.lingshot.completephrase_presentation.CompletePhraseEvent.UpdatePhrasePositionOnSuccess
import com.lingshot.completephrase_presentation.CompletePhraseUiState
import com.lingshot.completephrase_presentation.CompletePhraseViewModel
import com.lingshot.completephrase_presentation.R
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseAnswerSheet
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseAnswersFinishedFullScreenDialog
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseCollectionEmpty
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseIndicatorPage
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseTextFieldCard
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseTranslateCard
import com.lingshot.designsystem.component.LingshotLayout
import com.lingshot.designsystem.component.LingshotLoading
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.domain.helper.FormatPhraseHelper.extractWordsInDoubleParentheses
import com.lingshot.domain.helper.FormatPhraseHelper.processPhraseWithDoubleParentheses
import com.lingshot.reviewlevel_domain.model.ReviewLevel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun CompletePhraseScreenRoute(
    languageId: String,
    languageFrom: String,
    @Suppress("UNUSED_PARAMETER") languageTo: String,
    onBackClick: () -> Unit,
    viewModel: CompletePhraseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchPhrasesByLanguageCollections(languageId)
    }

    CompletePhraseScreen(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
        languageId = languageId,
        languageFrom = languageFrom,
        onBackClick = onBackClick,
    )
}

@Composable
internal fun CompletePhraseScreen(
    uiState: CompletePhraseUiState,
    handleEvent: (CompletePhraseEvent) -> Unit,
    languageId: String,
    languageFrom: String,
    onBackClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var currentPageIndex by remember { mutableIntStateOf(0) }
    val currentPage = (currentPageIndex + 1)

    LingshotLayout(
        title = stringResource(R.string.text_title_complete_phrase),
        onClickNavigation = onBackClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (uiState.isLoading) {
                LingshotLoading(modifier = Modifier.align(Alignment.Center))
            } else {
                if (uiState.phrasesByLanguageCollections.isNotEmpty()) {
                    val phraseDomain = uiState.phrasesByLanguageCollections[currentPageIndex]

                    val listWords = processPhraseWithDoubleParentheses(
                        phraseDomain.original,
                    ).toImmutableList()

                    val wordWithoutParentheses =
                        extractWordsInDoubleParentheses(phraseDomain.original)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                    ) {
                        CompletePhraseIndicatorPage(
                            currentPage,
                            uiState.phrasesByLanguageCollections.size,
                        )
                        key(currentPageIndex) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CompletePhraseTextFieldCard(
                                    listWords = listWords,
                                    wordWithoutParentheses = wordWithoutParentheses,
                                    wordToFill = uiState.wordToFill,
                                    onFillWord = { word ->
                                        handleEvent(FillWord(word))
                                    },
                                    isSpeechActive = uiState.isSpeechActive,
                                    onSpeakText = {
                                        handleEvent(
                                            FetchTextToSpeech(phraseDomain.original, languageFrom),
                                        )
                                    },
                                    reviewLevel = ReviewLevel.from(phraseDomain.reviewLevel),
                                )
                                CompletePhraseTranslateCard(
                                    translateText = phraseDomain.translate,
                                    isTranslatedTextVisible = uiState.isTranslatedTextVisible,
                                    onToggleTranslatedTextVisibility = {
                                        handleEvent(
                                            ToggleTranslatedTextVisibility,
                                        )
                                    },
                                )
                            }
                        }
                    }

                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        text = {
                            Text(text = stringResource(R.string.text_button_verify_complete_phrase))
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.SkipNext, contentDescription = null)
                        },
                        onClick = {
                            if (uiState.isSpeechActive.not()) {
                                val isAnswerCorrect = uiState.wordToFill.equals(
                                    wordWithoutParentheses,
                                    ignoreCase = true,
                                )
                                handleEvent(ShowAnswerSheet(isAnswerCorrect))
                            }
                        },
                    )

                    if (uiState.isAnswerSheetVisible) {
                        CompletePhraseAnswerSheet(
                            answerState = uiState.answerState,
                            correctAnswer = wordWithoutParentheses,
                            onContinue = {
                                if (uiState.answerState.isSuccess) {
                                    handleEvent(
                                        UpdatePhrasePositionOnSuccess(languageId, phraseDomain),
                                    )
                                } else {
                                    scope.launch {
                                        handleEvent(UpdatePhrasePositionOnError(phraseDomain))
                                    }.invokeOnCompletion {
                                        handleEvent(ClearState)
                                        currentPageIndex = currentPage
                                    }
                                }
                            },
                            onDismiss = {
                                handleEvent(HideAnswerSheet)
                            },
                        )
                    }
                } else {
                    CompletePhraseCollectionEmpty(modifier = Modifier.align(Alignment.Center))
                }

                if (uiState.isAnswersFinished) {
                    CompletePhraseAnswersFinishedFullScreenDialog(
                        onDismiss = onBackClick,
                    )
                }
            }
        }
    }

    uiState.updatePhraseInLanguageCollectionsStatus.onSuccess {
        handleEvent(ClearState)
        if (currentPage <= (uiState.phrasesByLanguageCollections.size - 1)) {
            currentPageIndex = currentPage
        } else {
            handleEvent(FetchAnswersFinished)
        }
    }

    LaunchedEffect(currentPageIndex, uiState.isAnswerSheetVisible) {
        if (currentPageIndex != 0 && scrollState.value != 0) {
            scrollState.animateScrollTo(0)
        }
        if (uiState.isAnswerSheetVisible) {
            handleEvent(FetchAnswerSound)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseScreenPreview() {
    LingshotTheme(isDarkTheme = true) {
        CompletePhraseScreen(
            uiState = CompletePhraseUiState(),
            handleEvent = {},
            languageId = "id",
            languageFrom = "en",
            onBackClick = {},
        )
    }
}
