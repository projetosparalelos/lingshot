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
package com.lingshot.designsystem.component

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.CropCornerShape.RECTANGLE
import com.canhub.cropper.CropImageView.Guidelines.OFF
import com.lingshot.common.util.findActivity
import com.lingshot.designsystem.R
import com.lingshot.designsystem.component.ActionCropImage.CROPPED_IMAGE
import com.lingshot.designsystem.component.ActionCropImage.FOCUS_IMAGE
import com.lingshot.designsystem.theme.md_theme_dark_tertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LingshotCropImage(
    actionCropImage: ActionCropImage?,
    onCroppedImage: (ActionCropImage?) -> Unit,
    modifier: Modifier = Modifier,
    isAutomaticCropperEnabled: Boolean = false,
    isRunnable: Boolean = false,
    imageUri: Uri? = rememberImageUriPath(),
    content: @Composable BoxScope.(Rect) -> Unit = {},
    onClear: () -> Unit = {},
    onCropImageResult: (Bitmap?) -> Unit,
) {
    val cropImage = rememberCropImage()
    val scope = rememberCoroutineScope()
    var copyRect by remember { mutableStateOf(cropImage.cropRect) }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                cropImage.apply {
                    setImageUriAsync(imageUri)
                    cropRectDefault()
                }
            },
        ) { cropImageView ->
            when (actionCropImage) {
                CROPPED_IMAGE -> {
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

            if (isAutomaticCropperEnabled) {
                cropImageView.setOnSetCropOverlayReleasedListener {
                    scope.launch {
                        delay(500.milliseconds)
                        if (copyRect != it && isRunnable.not()) {
                            onCroppedImage(CROPPED_IMAGE)
                            copyRect = it
                        }
                    }
                }
                cropImageView.setOnSetCropOverlayMovedListener {
                    onClear()
                }
            }
        }
        copyRect?.let { content(it) }
    }
    LaunchedEffect(actionCropImage) {
        if (actionCropImage != null) {
            onCroppedImage(null)
        }
    }
}

@Composable
@Suppress("Deprecation")
fun rememberImageUriPath(context: Context = LocalContext.current) = remember {
    val activity = context.findActivity()
    val intent = activity?.intent
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
    } else {
        intent?.getParcelableExtra(Intent.EXTRA_STREAM)
    }
}

@Composable
fun uriToBitmap(context: Context, uri: Uri? = rememberImageUriPath()): Bitmap? {
    val contentResolver: ContentResolver = context.contentResolver

    return runCatching {
        uri?.let { newUri ->
            contentResolver.openInputStream(newUri)?.use {
                BitmapFactory.decodeStream(it)
            }
        }
    }.getOrNull()
}

@Composable
private fun rememberCropImage(context: Context = LocalContext.current) = remember {
    val borderCornerColor = md_theme_dark_tertiary.toArgb()
    CropImageView(context).apply {
        val cropImageOptions = CropImageOptions(
            borderLineThickness = 0f,
            borderCornerOffset = applyDimension(COMPLEX_UNIT_DIP, 1f, Resources.getSystem().displayMetrics),
            borderCornerThickness = applyDimension(COMPLEX_UNIT_DIP, 4f, Resources.getSystem().displayMetrics),
            borderCornerColor = borderCornerColor,
            backgroundColor = Color.argb(50, 0, 0, 0),
            guidelines = OFF,
            cornerShape = RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
        )
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setImageCropOptions(cropImageOptions)
    }
}

private fun CropImageView.cropRectDefault() {
    val sideLength = applyDimension(COMPLEX_UNIT_DIP, 100f, Resources.getSystem().displayMetrics).toInt()
    val screenWidth = resources.displayMetrics.widthPixels
    val screenHeight = resources.displayMetrics.heightPixels

    val rectTop = (screenHeight - sideLength) / 2
    val rectBottom = rectTop + sideLength
    val rectLeft = screenWidth - sideLength

    cropRect = Rect(rectLeft, rectTop, screenWidth, rectBottom)
}

@Preview(showBackground = true)
@Composable
private fun LingshotCropImagePreview() {
    val resources = LocalContext.current.resources
    val cropImagePreview = R.drawable.crop_image_preview

    val imageUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(cropImagePreview))
        .appendPath(resources.getResourceTypeName(cropImagePreview))
        .appendPath(resources.getResourceEntryName(cropImagePreview))
        .build()

    LingshotCropImage(
        imageUri = imageUri,
        actionCropImage = FOCUS_IMAGE,
        onCroppedImage = {},
        onCropImageResult = {},
    )
}

enum class ActionCropImage {
    CROPPED_IMAGE,
    FOCUS_IMAGE,
}
