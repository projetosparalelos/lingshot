package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.CropCornerShape.OVAL
import com.canhub.cropper.CropImageView.Guidelines.OFF
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType
import timber.log.Timber

@Composable
fun ScreenShotCropImage(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    actionCropImageType: ActionCropImageType?,
    onCroppedImage: (ActionCropImageType?) -> Unit,
    onCropImageResult: (Bitmap?) -> Unit
) {
    val cropImage = rememberCropImage()

    AndroidView(
        modifier = modifier,
        factory = {
            cropImage.apply {
                setImageUriAsync(imageUri)
                cropRectDefault()
            }
        },
    ) { cropImageView ->
        when (actionCropImageType) {
            ActionCropImageType.CROPPED_IMAGE -> {
                cropImage.croppedImageAsync()
            }
            ActionCropImageType.FOCUS_IMAGE -> {
                cropImage.cropRect = Rect(null)
            }
            else -> { Timber.i("Clear crop action of image.") }
        }
        cropImageView.setOnCropImageCompleteListener { _, result ->
            onCropImageResult(result.bitmap)
        }
    }
    LaunchedEffect(actionCropImageType) {
        onCroppedImage(null)
    }
}

@Composable
private fun rememberCropImage(context: Context = LocalContext.current) = remember {
    CropImageView(context).apply {
        val cropImageOptions = CropImageOptions(
            guidelines = OFF,
            cornerShape = OVAL,
            showProgressBar = false
        )
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setImageCropOptions(cropImageOptions)
    }
}

private fun CropImageView.cropRectDefault() {
    val rectRight = 500
    val rectBottom = 450
    val width = resources.displayMetrics.widthPixels
    cropRect = Rect(width - rectRight, 0, width, rectBottom)
}