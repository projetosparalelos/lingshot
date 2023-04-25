package com.teachmeprint.language.feature.screenshot.model.event

import android.graphics.Bitmap
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum

sealed class ScreenShotEvent {
    object CroppedImage : ScreenShotEvent()
    data class ShowBalloon(val textTranslate: String) : ScreenShotEvent()
    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?,
        val typeIndicatorEnum: TypeIndicatorEnum? = null
    ) : ScreenShotEvent()
}