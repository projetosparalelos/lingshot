package com.teachmeprint.language.feature.screenshot.model.event

import android.graphics.Bitmap
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType

sealed class ScreenShotEvent {
    data class CroppedImage(val actionCropImageType: ActionCropImageType?) : ScreenShotEvent()
    data class ShowBalloon(val textTranslate: String) : ScreenShotEvent()
    data class ToggleTypeIndicatorEnum(val typeIndicatorEnum: TypeIndicatorEnum) : ScreenShotEvent()

    data class FetchTextRecognizer(
        val imageBitmap: Bitmap?
    ) : ScreenShotEvent()
}