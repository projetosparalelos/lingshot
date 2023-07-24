package com.lingshot.screenshot_presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.common.helper.onEmpty
import com.lingshot.common.helper.onError
import com.lingshot.common.helper.onLoading
import com.lingshot.common.helper.onSuccess
import com.lingshot.languagechoice_presentation.ui.LanguageChoiceDialog
import com.lingshot.screenshot_presentation.R
import com.lingshot.screenshot_presentation.ScreenShotEvent
import com.lingshot.screenshot_presentation.ScreenShotEvent.CheckPhraseInLanguageCollection
import com.lingshot.screenshot_presentation.ScreenShotEvent.ClearStatus
import com.lingshot.screenshot_presentation.ScreenShotEvent.CroppedImage
import com.lingshot.screenshot_presentation.ScreenShotEvent.FetchCorrectedOriginalText
import com.lingshot.screenshot_presentation.ScreenShotEvent.FetchTextRecognizer
import com.lingshot.screenshot_presentation.ScreenShotEvent.SaveLanguage
import com.lingshot.screenshot_presentation.ScreenShotEvent.SavePhraseInLanguageCollection
import com.lingshot.screenshot_presentation.ScreenShotEvent.SelectedOptionsLanguage
import com.lingshot.screenshot_presentation.ScreenShotEvent.SelectedOptionsNavigationBar
import com.lingshot.screenshot_presentation.ScreenShotEvent.ToggleDictionaryFullScreenPopup
import com.lingshot.screenshot_presentation.ScreenShotEvent.ToggleLanguageDialog
import com.lingshot.screenshot_presentation.ScreenShotEvent.ToggleLanguageDialogAndHideSelectionAlert
import com.lingshot.screenshot_presentation.ScreenShotUiState
import com.lingshot.screenshot_presentation.ScreenShotViewModel
import com.lingshot.screenshot_presentation.ScreenShotViewModel.Companion.ILLEGIBLE_TEXT
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.TRANSLATE
import com.lingshot.screenshot_presentation.ui.component.ScreenShotBalloon
import com.lingshot.screenshot_presentation.ui.component.ScreenShotCropImage
import com.lingshot.screenshot_presentation.ui.component.ScreenShotDictionaryFullScreenPopup
import com.lingshot.screenshot_presentation.ui.component.ScreenShotLottieLoading
import com.lingshot.screenshot_presentation.ui.component.ScreenShotNavigationBar
import com.lingshot.screenshot_presentation.ui.component.ScreenShotNavigationBarItem
import com.lingshot.screenshot_presentation.ui.component.ScreenShotSnackBarError
import com.lingshot.screenshot_presentation.ui.component.ScreenShotSnackBarSelectLanguage
import com.lingshot.screenshot_presentation.ui.component.ScreenShotTranslateBottomSheet
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ScreenShotRoute(
    viewModel: ScreenShotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenShotScreen(
        uiState = uiState,
        handleEvent = viewModel::handleEvent
    )
}

@Composable
private fun ScreenShotScreen(
    uiState: ScreenShotUiState,
    modifier: Modifier = Modifier,
    handleEvent: (event: ScreenShotEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color.Black)
    ) {
        ScreenShotCropImage(
            actionCropImage = uiState.actionCropImage,
            onCropImageResult = { bitmap ->
                handleEvent(FetchTextRecognizer(bitmap))
            },
            onCroppedImage = {
                handleEvent(CroppedImage(it))
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            uiState.screenShotStatus.onEmpty {
                ScreenShotBalloon(
                    text = ILLEGIBLE_TEXT,
                    onDismiss = {
                        handleEvent(ClearStatus)
                    }
                )
            }.onLoading {
                val loading = uiState.navigationBarItem
                    .takeIf { it == TRANSLATE }
                    ?.let { R.raw.loading_translate } ?: R.raw.loading_listen

                ScreenShotLottieLoading(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    loading = loading
                )
            }.onSuccess {
                if (uiState.navigationBarItem == TRANSLATE) {
                    ScreenShotTranslateBottomSheet(
                        languageTranslationDomain = it,
                        isPhraseSaved = uiState.isPhraseSaved,
                        correctedOriginalTextStatus = uiState.correctedOriginalTextStatus,
                        onCorrectedOriginalText = { original ->
                            handleEvent(FetchCorrectedOriginalText(original))
                        },
                        onCheckPhraseInLanguageCollection = { originalText ->
                            handleEvent(
                                CheckPhraseInLanguageCollection(originalText)
                            )
                        },
                        onSavePhraseInLanguageCollection = { originalText, translatedText ->
                            handleEvent(
                                SavePhraseInLanguageCollection(originalText, translatedText)
                            )
                        },
                        onToggleDictionaryFullScreenPopup = { url ->
                            handleEvent(ToggleDictionaryFullScreenPopup(url))
                        },
                        onDismiss = {
                            handleEvent(ClearStatus)
                        }
                    )
                }
            }.onError {
                ScreenShotSnackBarError(
                    modifier = Modifier.padding(bottom = 16.dp),
                    message = it,
                    onDismiss = {
                        handleEvent(ClearStatus)
                    }
                )
            }
            if (uiState.isLanguageSelectionAlertVisible) {
                ScreenShotSnackBarSelectLanguage(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onToggleLanguageDialogAndHideSelectionAlert = {
                        handleEvent(ToggleLanguageDialogAndHideSelectionAlert)
                    }
                )
            }
            ScreenShotNavigationBar(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                ScreenShotNavigationBarItem(
                    navigationBarItem = uiState.navigationBarItem,
                    navigationBarItemList = uiState.navigationBarItemList,
                    onSelectedOptionsNavigationBar = { item ->
                        if (!uiState.screenShotStatus.isLoadingStatus) {
                            handleEvent(SelectedOptionsNavigationBar(item))
                        }
                    }
                )
            }
        }
    }
    if (uiState.isLanguageDialogVisible) {
        LanguageChoiceDialog(
            availableLanguage = uiState.availableLanguage,
            availableLanguageList = uiState.availableLanguageList.toImmutableList(),
            onSaveLanguage = {
                handleEvent(SaveLanguage(it))
            },
            onSelectedOptionsLanguage = {
                handleEvent(SelectedOptionsLanguage(it))
            },
            onDismiss = {
                handleEvent(ToggleLanguageDialog)
            }
        )
    }

    uiState.dictionaryUrl?.let { url ->
        ScreenShotDictionaryFullScreenPopup(url) {
            handleEvent(ToggleDictionaryFullScreenPopup(null))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotScreenPreview() {
    ScreenShotScreen(
        uiState = ScreenShotUiState(),
        handleEvent = {}
    )
}