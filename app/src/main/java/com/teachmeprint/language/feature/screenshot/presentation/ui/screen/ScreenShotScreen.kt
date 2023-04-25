package com.teachmeprint.language.feature.screenshot.presentation.ui.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.feature.screenshot.model.event.ScreenShotEvent
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotStatus
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotUiState
import com.teachmeprint.language.feature.screenshot.presentation.ScreenShotViewModel
import com.teachmeprint.language.feature.screenshot.presentation.ui.component.ScreenShotBalloon
import com.teachmeprint.language.feature.screenshot.presentation.ui.component.ScreenShotCropImage
import com.teachmeprint.language.feature.screenshot.presentation.ui.component.ScreenShotNavigationBar
import com.teachmeprint.language.ui.theme.TeachMePrintTheme

@Composable
fun ScreenShotRoute(viewModel: ScreenShotViewModel = hiltViewModel(), imageUri: Uri?) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachMePrintTheme {
        ScreenShotScreen(
            imageUri = imageUri,
            uiState = uiState,
            handleEvent = viewModel::handleEvent
        )
    }
}

@Composable
fun ScreenShotScreen(
    imageUri: Uri? = null,
    uiState: ScreenShotUiState,
    handleEvent: (event: ScreenShotEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ScreenShotCropImage(imageUri = imageUri,
            croppedImage = uiState.croppedImage,
            onCropImageResult = {
                handleEvent(ScreenShotEvent.FetchTextRecognizer(it, TypeIndicatorEnum.TRANSLATE))
            },
            onCroppedImage = {
                handleEvent(ScreenShotEvent.CroppedImage)
            }
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            ScreenShotBalloon(
                text = uiState.textTranslate,
                showBalloon = uiState.showBalloon,
                onShowBalloon = {
                    handleEvent(ScreenShotEvent.ShowBalloon(""))
                }
            )
            ScreenShotNavigationBar(
                navigationBarItemsType = uiState.navigationBarItemsType,
                onCroppedImage = {
                    handleEvent(ScreenShotEvent.CroppedImage)
                }
            )
        }
    }
    ScreenShotStatus(
        screenShotStatus = uiState.screenShotStatus,
        onShowBalloon = {
            handleEvent(ScreenShotEvent.ShowBalloon(it))
        })
}


@Composable
fun ScreenShotStatus(
    screenShotStatus: ScreenShotStatus,
    onShowBalloon: (String) -> Unit
) {
    when (screenShotStatus) {
        is ScreenShotStatus.Success -> {
            screenShotStatus.text?.let {
                onShowBalloon(it)
            }
        }
        is ScreenShotStatus.Error -> {}
        is ScreenShotStatus.Loading -> {}
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_ScreenShotScreen() {
    ScreenShotScreen(uiState = ScreenShotUiState(), handleEvent = {})
}