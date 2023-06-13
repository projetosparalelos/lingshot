package com.teachmeprint.swipepermission_presentation.ui.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun SwipePermissionAnimationIcon(
    @RawRes icon: Int,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(icon)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = IterateForever
    )
    LottieAnimation(
        modifier = modifier
            .width(350.dp)
            .height(350.dp),
        composition = composition,
        progress = { progress }
    )
}