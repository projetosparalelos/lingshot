package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.CropCornerShape.OVAL
import com.canhub.cropper.CropImageView.Guidelines.OFF

@Composable
fun ScreenShotCropImage(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    croppedImage: Boolean,
    onCroppedImage: () -> Unit,
    onCropImageResult: (Bitmap?) -> Unit
) {
    val cropImage = rememberCropImage()

    AndroidView(
        modifier = modifier,
        factory = {
            cropImage.apply {
                setImageUriAsync(imageUri)
                cropRectCustom()
            }
        },
    ) { cropImageView ->
        if (croppedImage) {
            cropImageView.croppedImageAsync()
        }
        cropImageView.setOnCropImageCompleteListener { _, result ->
            onCropImageResult(result.bitmap)
            onCroppedImage()
        }
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

private fun CropImageView.cropRectCustom() {
    val rectRight = 500
    val rectBottom = 450
    val width = resources.displayMetrics.widthPixels
    cropRect = Rect(width - rectRight, 0, width, rectBottom)
}