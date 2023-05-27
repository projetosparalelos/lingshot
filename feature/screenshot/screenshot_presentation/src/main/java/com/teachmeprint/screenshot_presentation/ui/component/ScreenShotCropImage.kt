package com.teachmeprint.screenshot_presentation.ui.component

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.CropCornerShape.OVAL
import com.canhub.cropper.CropImageView.Guidelines.OFF
import com.teachmeprint.common.util.findActivity
import com.teachmeprint.screenshot_presentation.R
import com.teachmeprint.screenshot_presentation.ui.component.ActionCropImage.FOCUS_IMAGE
import timber.log.Timber

@Composable
fun ScreenShotCropImage(
    actionCropImage: ActionCropImage?,
    onCroppedImage: (ActionCropImage?) -> Unit,
    modifier: Modifier = Modifier,
    imageUri: Uri? = rememberImageUriPath(),
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
        }
    ) { cropImageView ->
        when (actionCropImage) {
            ActionCropImage.CROPPED_IMAGE -> {
                cropImage.croppedImageAsync()
            }

            FOCUS_IMAGE -> {
                cropImage.cropRect = Rect(null)
            }

            else -> {
                Timber.i("Clear crop action of image.")
            }
        }
        cropImageView.setOnCropImageCompleteListener { _, result ->
            onCropImageResult(result.bitmap)
        }
    }
    LaunchedEffect(actionCropImage) {
        onCroppedImage(null)
    }
}

@Composable
@Suppress("Deprecation")
private fun rememberImageUriPath(context: Context = LocalContext.current) = remember {
    val activity = context.findActivity()
    val intent = activity?.intent
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
    } else {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM)
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

@Preview(showBackground = true)
@Composable
private fun ScreenShotCropImagePreview() {
    val resources = LocalContext.current.resources
    val cropImagePreview = R.drawable.crop_image_preview

    val imageUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(cropImagePreview))
        .appendPath(resources.getResourceTypeName(cropImagePreview))
        .appendPath(resources.getResourceEntryName(cropImagePreview))
        .build()

    ScreenShotCropImage(
        imageUri = imageUri,
        actionCropImage = FOCUS_IMAGE,
        onCroppedImage = {},
        onCropImageResult = {}
    )
}

enum class ActionCropImage {
    CROPPED_IMAGE,
    FOCUS_IMAGE
}