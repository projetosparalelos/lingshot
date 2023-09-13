package com.lingshot.screenshot_presentation.ui.component

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
import com.lingshot.common.util.limitCharactersWithEllipsize
import com.skydoves.balloon.BalloonAnimation.ELASTIC
import com.skydoves.balloon.BalloonCenterAlign.START
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor

@Composable
internal fun ScreenShotBalloon(
    text: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Balloon(
        modifier = modifier,
        builder = rememberBalloonBuilder(MaterialTheme.colorScheme.primaryContainer),
        balloonContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = text.limitCharactersWithEllipsize(280)
            )
        }
    ) { balloonWindow ->
        balloonWindow.showAtCenter(centerAlign = START)
        balloonWindow.setOnBalloonDismissListener(onDismiss)
        Divider(color = Color.Transparent)
    }
}

@Composable
private fun rememberBalloonBuilder(background: Color) = rememberBalloonBuilder {
    setArrowPosition(0.16f)
    setMarginHorizontal(16)
    setPadding(12)
    setCornerRadius(24f)
    setBalloonAnimation(ELASTIC)
    setBackgroundColor(background)
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotBalloonPreview() {
    ScreenShotBalloon(text = "Balloon Preview") {}
}
