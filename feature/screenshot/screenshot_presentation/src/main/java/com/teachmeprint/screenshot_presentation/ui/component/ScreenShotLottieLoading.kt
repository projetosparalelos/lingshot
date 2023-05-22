package com.teachmeprint.screenshot_presentation.ui.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.teachmeprint.screenshot_presentation.R

@Composable
fun ScreenShotLottieLoading(
    modifier: Modifier = Modifier,
    @RawRes loading: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = modifier
            .width(100.dp)
            .height(100.dp),
        composition = composition,
        progress = { progress }
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotLottieLoadingPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ScreenShotLottieLoading(
            modifier = Modifier.align(Alignment.TopCenter),
            loading = R.raw.loading_translate
        )
    }
}