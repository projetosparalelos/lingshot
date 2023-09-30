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
package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lingshot.designsystem.util.noRippleClickable
import com.lingshot.home_presentation.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeToggleServiceButton(
    isServiceRunning: Boolean,
    onToggleServiceButton: () -> Unit,
    modifier: Modifier = Modifier,
    onFinishActivity: () -> Unit,
) {
    var firstTime by remember { mutableStateOf(true) }

    val on = LottieClipSpec.Progress(0f, 0.5f)
    val off = LottieClipSpec.Progress(0.5f, 1f)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.toggle_service_button_animation),
    )
    val anim = rememberLottieAnimatable()

    LottieAnimation(
        modifier = modifier
            .size(width = 150.dp, height = 60.dp)
            .noRippleClickable(onToggleServiceButton),
        contentScale = ContentScale.Crop,
        composition = composition,
        progress = { anim.progress },
    )

    LaunchedEffect(isServiceRunning) {
        if (firstTime) {
            anim.snapTo(
                composition = composition,
                progress = if (isServiceRunning) 0.5f else 0f,
            )
            delay(200.milliseconds)
            firstTime = false
        } else {
            anim.animate(
                composition = composition,
                speed = 1.5f,
                clipSpec = if (isServiceRunning) on else off,
            )
            if (anim.progress == 0.5f) {
                onFinishActivity()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeToggleServiceButtonPreview() {
    HomeToggleServiceButton(
        isServiceRunning = false,
        onToggleServiceButton = {},
        onFinishActivity = {},
    )
}
