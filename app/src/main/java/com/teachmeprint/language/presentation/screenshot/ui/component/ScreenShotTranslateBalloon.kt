package com.teachmeprint.language.presentation.screenshot.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.skydoves.balloon.BalloonAnimation.ELASTIC
import com.skydoves.balloon.BalloonCenterAlign.START
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.teachmeprint.language.core.common.util.limitCharactersWithEllipsize
import com.teachmeprint.language.core.designsystem.theme.OthersButton

@Composable
fun ScreenShotTranslateBalloon(
    modifier: Modifier = Modifier,
    text: String,
    onHideTranslateBalloon: () -> Unit
) {
    Balloon(
        modifier = modifier,
        builder = rememberBalloonBuilder(),
        balloonContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                text = text.limitCharactersWithEllipsize(280)
            )
        }
    ) { balloonWindow ->
        balloonWindow.showAtCenter(centerAlign = START)
        balloonWindow.setOnBalloonDismissListener(onHideTranslateBalloon)
        Divider(color = Color.Transparent)
    }
}

@Composable
private fun rememberBalloonBuilder() = rememberBalloonBuilder {
    setArrowPosition(0.16f)
    setMarginHorizontal(16)
    setPadding(12)
    setCornerRadius(24f)
    setBalloonAnimation(ELASTIC)
    setBackgroundColor(OthersButton)
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotTranslateBalloonPreview() {
    ScreenShotTranslateBalloon(text = "Balloon Preview") {}
}