package com.teachmeprint.home_presentation.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.teachmeprint.designsystem.util.noRippleClickable
import com.teachmeprint.home_presentation.R
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@Composable
fun HomeToggleServiceButton(
    isServiceRunning: Boolean,
    onToggleServiceButton: () -> Unit,
    modifier: Modifier = Modifier,
    onFinishActivity: () -> Unit
) {
    var firstTime by remember { mutableStateOf(true) }

    val on = LottieClipSpec.Progress(0f, 0.5f)
    val off = LottieClipSpec.Progress(0.5f, 1f)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.toggle_service_button_animation)
    )
    val anim = rememberLottieAnimatable()

    LottieAnimation(
        modifier = modifier
            .size(150.dp)
            .noRippleClickable(onToggleServiceButton),
        composition = composition,
        progress = { anim.progress }
    )

    LaunchedEffect(isServiceRunning) {
        if (firstTime) {
            anim.snapTo(
                composition = composition,
                progress = if (isServiceRunning) 0.5f else 0f
            )
            delay(200.milliseconds)
            firstTime = false
        } else {
            anim.animate(
                composition = composition,
                speed = 1.5f,
                clipSpec = if (isServiceRunning) on else off
            )
            if (anim.progress == 0.5f) {
                onFinishActivity()
            }
        }
    }
}