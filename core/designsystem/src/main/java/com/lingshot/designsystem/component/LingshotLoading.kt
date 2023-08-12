package com.lingshot.designsystem.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lingshot.designsystem.R

@Composable
fun LingshotLoading(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lingshot_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = IterateForever
    )
    LottieAnimation(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.5f),
        composition = composition,
        progress = { progress }
    )
}
