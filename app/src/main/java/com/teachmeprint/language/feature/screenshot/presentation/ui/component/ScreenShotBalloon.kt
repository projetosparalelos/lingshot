package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.skydoves.balloon.BalloonAnimation.ELASTIC
import com.skydoves.balloon.BalloonCenterAlign.START
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.teachmeprint.language.core.util.limitCharactersWithEllipsize
import com.teachmeprint.language.ui.theme.OthersButton

@Composable
fun ScreenShotBalloon(
    modifier: Modifier = Modifier,
    text: String,
    showBalloon: Boolean,
    onShowBalloon: () -> Unit
) {
    Balloon(
        modifier = modifier,
        builder = rememberBalloonBuilder(),
        balloonContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                text = text.limitCharactersWithEllipsize(280)
            )
        }
    ) { balloonWindow ->
        if (showBalloon) {
            balloonWindow.showAtCenter(centerAlign = START)
        }
        balloonWindow.setOnBalloonDismissListener {
            onShowBalloon()
        }
        Divider(color = Color.Transparent)
    }
}

@Composable
private fun rememberBalloonBuilder() = rememberBalloonBuilder {
    setArrowPosition(0.14f)
    setMarginHorizontal(16)
    setPadding(12)
    setCornerRadius(24f)
    setBalloonAnimation(ELASTIC)
    setBackgroundColor(OthersButton)
}