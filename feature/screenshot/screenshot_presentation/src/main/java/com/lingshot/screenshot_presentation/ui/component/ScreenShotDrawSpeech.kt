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
package com.lingshot.screenshot_presentation.ui.component

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lingshot.common.util.decodeHtmlString
import com.lingshot.designsystem.component.uriToBitmap
import com.lingshot.screenshot_presentation.R
import kotlin.math.min

@Composable
internal fun ScreenShotDrawSpeech(
    boundingBox: Rect,
    translatedText: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val textMeasurer = rememberTextMeasurer()
    val bitmap = uriToBitmap(context)

    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            val drawOffsetY = (size.height - (bitmap?.height ?: 0)) / 2

            val adjustedRect = Rect(
                boundingBox.left,
                (boundingBox.top + drawOffsetY).toInt(),
                boundingBox.right,
                (boundingBox.bottom + drawOffsetY).toInt(),
            )

            drawSpeechBoundingBox(
                boundingBox = adjustedRect,
                text = translatedText.decodeHtmlString(),
                textMeasurer = textMeasurer,
            )
        },
    )
}

private fun DrawScope.drawSpeechBoundingBox(
    boundingBox: Rect,
    text: String,
    textMeasurer: TextMeasurer,
) {
    val currentWidth = textMeasurer.measure(text).size.width
    val currentHeight = textMeasurer.measure(text).size.height
    val desiredWidth = boundingBox.width()
    val desiredHeight = boundingBox.height()
    val marginBottom = 30
    val centerText = (boundingBox.height() - currentHeight) / 2 - marginBottom

    val font = calculateScaledFontSize(
        text = text,
        currentWidth = currentWidth,
        currentHeight = currentHeight,
        desiredWidth = desiredWidth,
        desiredHeight = desiredHeight,
        minFontSize = 14.sp.toPx(),
        maxFontSize = 14.sp.value,
    )

    val style = TextStyle(
        fontSize = font.sp,
        fontFamily = FontFamily(Font(R.font.manga_master_bb)),
        color = Color.Black,
        background = Color.White,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )

    drawOval(
        color = Color.White,
        topLeft = Offset(boundingBox.left.toFloat(), boundingBox.top.toFloat()),
        size = Size(boundingBox.width().toFloat(), boundingBox.height().toFloat()),
    )
    drawOval(
        color = Color.Black,
        style = Stroke(width = 2f),
        topLeft = Offset(boundingBox.left.toFloat(), boundingBox.top.toFloat()),
        size = Size(boundingBox.width().toFloat(), boundingBox.height().toFloat()),
    )
    drawText(
        textMeasurer = textMeasurer,
        style = style,
        size = Size(boundingBox.width().toFloat(), boundingBox.height().toFloat()),
        topLeft = Offset(boundingBox.left.toFloat(), boundingBox.top.toFloat() + centerText),
        text = text.uppercase(),
    )
}

@Suppress("LongParameterList")
private fun calculateScaledFontSize(
    text: String,
    currentWidth: Int,
    currentHeight: Int,
    desiredWidth: Int,
    desiredHeight: Int,
    minFontSize: Float,
    maxFontSize: Float,
): Float {
    val widthScaleFactor = minFontSize * desiredWidth / currentWidth
    val heightScaleFactor = minFontSize * desiredHeight / currentHeight
    val safeFontSize = 10f

    return if (min(widthScaleFactor, heightScaleFactor) >= maxFontSize) {
        if (text.length <= 20) {
            min(widthScaleFactor, heightScaleFactor) / 4
        } else {
            min(widthScaleFactor, heightScaleFactor) / 2
        }
    } else {
        if (text.length >= 70) {
            safeFontSize
        } else {
            min(widthScaleFactor, heightScaleFactor)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotDrawSpeechPreview() {
    ScreenShotDrawSpeech(
        boundingBox = Rect(0, 0, 100, 100),
        translatedText = "Translated Text",
    )
}
