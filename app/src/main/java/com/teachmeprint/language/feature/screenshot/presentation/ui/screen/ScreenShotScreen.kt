package com.teachmeprint.language.feature.screenshot.presentation.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teachmeprint.language.core.util.findActivity
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
fun ScreenShotRoute(viewModel: ScreenShotViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachMePrintTheme {
        ScreenShotScreen(
            imageUri = rememberImageUriPath(),
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
private fun ScreenShotStatus(
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

@Composable
@Suppress("Deprecation")
private fun rememberImageUriPath(context: Context = LocalContext.current) = remember {
    val activity = context.findActivity()
    val intent = activity?.intent
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
    } else {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotScreenPreview() {
    ScreenShotScreen(uiState = ScreenShotUiState(), handleEvent = {})
}