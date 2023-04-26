package com.teachmeprint.language.feature.screenshot.presentation.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teachmeprint.language.core.util.findActivity
import com.teachmeprint.language.feature.screenshot.model.event.ScreenShotEvent
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotStatus
import com.teachmeprint.language.feature.screenshot.model.state.ScreenShotUiState
import com.teachmeprint.language.feature.screenshot.presentation.ScreenShotViewModel
import com.teachmeprint.language.feature.screenshot.presentation.ui.component.*
import com.teachmeprint.language.ui.theme.TeachMePrintTheme

@Composable
fun ScreenShotRoute(viewModel: ScreenShotViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val systemUiController = rememberSystemUiController()

    TeachMePrintTheme {
        SideEffect {
            systemUiController.setStatusBarColor(Color.Black)
        }
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
    val status = uiState.screenShotStatus

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ScreenShotCropImage(imageUri = imageUri,
            actionCropImageType = uiState.actionCropImageType,
            onCropImageResult = { bitmap ->
                handleEvent(ScreenShotEvent.FetchTextRecognizer(bitmap))
            },
            onCroppedImage = {
                handleEvent(ScreenShotEvent.CroppedImage(it))
            }
        )
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
                    handleEvent(ScreenShotEvent.CroppedImage(it))
                },
                onToggleTypeIndicatorEnum = {
                    handleEvent(ScreenShotEvent.ToggleTypeIndicatorEnum(it))
                }
            )
        }
    }
    LaunchedEffect(status) {
        if (status is ScreenShotStatus.Success) {
            status.text?.let { handleEvent(ScreenShotEvent.ShowBalloon(it)) }
        }
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