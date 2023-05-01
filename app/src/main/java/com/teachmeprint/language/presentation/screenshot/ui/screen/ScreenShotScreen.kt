package com.teachmeprint.language.presentation.screenshot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teachmeprint.language.R
import com.teachmeprint.language.presentation.screenshot.ScreenShotEvent
import com.teachmeprint.language.presentation.screenshot.ScreenShotEvent.*
import com.teachmeprint.language.presentation.screenshot.ScreenShotStatus
import com.teachmeprint.language.presentation.screenshot.ScreenShotUiState
import com.teachmeprint.language.presentation.screenshot.ScreenShotViewModel
import com.teachmeprint.language.presentation.screenshot.ui.component.*
import com.teachmeprint.language.presentation.screenshot.ui.component.NavigationBarItem.TRANSLATE
import com.teachmeprint.language.core.designsystem.theme.TeachMePrintTheme

@Composable
fun ScreenShotRoute(
    viewModel: ScreenShotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachMePrintTheme {
        ScreenShotScreen(
            uiState = uiState,
            handleEvent = viewModel::handleEvent
        )
    }
}

@Composable
fun ScreenShotScreen(
    modifier: Modifier = Modifier,
    uiState: ScreenShotUiState,
    handleEvent: (event: ScreenShotEvent) -> Unit
) {
    val status = uiState.screenShotStatus

    Box(
        modifier = modifier
            .fillMaxSize()
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
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        if (status is ScreenShotStatus.Loading) {
            val loading = uiState.navigationBarItem
                .takeIf { it == TRANSLATE }
                ?.let { R.raw.loading_translate } ?: R.raw.loading_listen

            ScreenShotLottieLoading(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                loading = loading
            )
        }
        if (status is ScreenShotStatus.Error) {
            status.code?.let {
                ScreenShotSnackBarError(
                    modifier = Modifier.padding(bottom = 16.dp),
                    code = it
                )
            }
        }
        if (uiState.isLanguageSelectionAlertVisible) {
            ScreenShotSnackBarSelectLanguage(
                modifier = Modifier.padding(bottom = 16.dp),
                onToggleLanguageDialogAndHideSelectionAlert = {
                    handleEvent(ToggleLanguageDialogAndHideSelectionAlert)
                })
        }
        if (uiState.isBalloonTranslateVisible) {
            ScreenShotTranslateBalloon(
                text = uiState.textTranslate,
                onHideTranslateBalloon = {
                    handleEvent(HideTranslateBalloon)
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
                    if (status !is ScreenShotStatus.Loading) {
                        handleEvent(SelectedOptionsNavigationBar(item))
                    }
                }
            )
        }
    }
    if (uiState.isLanguageDialogVisible) {
        ScreenShotLanguageDialog(
            availableLanguage = uiState.availableLanguage,
            availableLanguageList = uiState.availableLanguageList,
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
    LaunchedEffect(status) {
        if ((status is ScreenShotStatus.Success) &&
            uiState.navigationBarItem == TRANSLATE
        ) {
            status.text?.let { handleEvent(ShowTranslateBalloon(it)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotScreenPreview() {
    ScreenShotScreen(
        uiState = ScreenShotUiState(),
        handleEvent = {})
}