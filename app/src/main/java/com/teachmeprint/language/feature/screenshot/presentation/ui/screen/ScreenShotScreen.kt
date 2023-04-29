package com.teachmeprint.language.feature.screenshot.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType.CROPPED_IMAGE
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType.FOCUS_IMAGE
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType.*
import com.teachmeprint.language.feature.screenshot.model.event.ScreenShotEvent
import com.teachmeprint.language.feature.screenshot.model.event.ScreenShotEvent.*
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotStatus
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotUiState
import com.teachmeprint.language.feature.screenshot.presentation.ScreenShotViewModel
import com.teachmeprint.language.feature.screenshot.presentation.ui.component.*
import com.teachmeprint.language.ui.theme.TeachMePrintTheme

@Composable
fun ScreenShotRoute(
    viewModel: ScreenShotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
            actionCropImageType = uiState.actionCropImageType,
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
            ScreenShotLottieLoading(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
        if (status is ScreenShotStatus.Error) {
            status.code?.let {
                ScreenShotSnackBarError(modifier = Modifier.padding(bottom = 16.dp), code = it)
            }
        }
        if (uiState.showBalloon) {
            ScreenShotBalloon(
                text = uiState.textTranslate,
                onShowBalloon = {
                    handleEvent(ShowBalloon(""))
                }
            )
        }
        ScreenShotNavigationBar(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            ScreenShotNavigationBarItem(
                navigationBarItemsType = uiState.navigationBarItemsType,
                selectedOptionNavigationBar = uiState.selectedOptionNavigationBar,
                onOptionSelectedNavigationBar = { item ->
                    if (status !is ScreenShotStatus.Loading) {
                        when (item) {
                            TRANSLATE -> {
                                handleEvent(CroppedImage(CROPPED_IMAGE))
                            }

                            LISTEN -> {
                                handleEvent(CroppedImage(CROPPED_IMAGE))
                            }

                            FOCUS -> {
                                handleEvent(CroppedImage(FOCUS_IMAGE))
                            }

                            LANGUAGE -> {
                                handleEvent(ShowDialogLanguage)
                            }
                        }
                        handleEvent(OptionSelectedNavigationBar(item))
                    }
                }
            )
        }
    }
    if (uiState.showDialogLanguage) {
        ScreenShotDialogLanguage(
            availableLanguages = uiState.availableLanguages,
            selectedOptionLanguage = uiState.selectedOptionLanguage,
            onOptionSelectedLanguage = {
                handleEvent(OptionSelectedLanguage(it))
            },
            onSaveLanguage = {
                handleEvent(SaveLanguage(it))
            },
            onDismiss = {
                handleEvent(ShowDialogLanguage)
            }
        )
    }
    LaunchedEffect(status) {
        if (status is ScreenShotStatus.Success) {
            status.text?.let { handleEvent(ShowBalloon(it)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotScreenPreview() {
    ScreenShotScreen(uiState = ScreenShotUiState(), handleEvent = {})
}