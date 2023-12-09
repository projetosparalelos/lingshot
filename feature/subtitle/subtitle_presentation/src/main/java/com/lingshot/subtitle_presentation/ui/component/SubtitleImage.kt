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
package com.lingshot.subtitle_presentation.ui.component

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.CropCornerShape.RECTANGLE
import com.canhub.cropper.CropImageView.Guidelines.OFF
import com.lingshot.common.R.drawable.crop_image_preview
import com.lingshot.common.util.findActivity
import com.lingshot.subtitle_presentation.ui.component.ActionCropImage.CROPPED_IMAGE
import com.lingshot.subtitle_presentation.ui.component.ActionCropImage.FOCUS_IMAGE
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable
import timber.log.Timber

@Composable
internal fun SubtitleImage(
    isImageWithZoom: Boolean,
    actionCropImage: ActionCropImage?,
    onCroppedImage: (ActionCropImage?) -> Unit,
    imageUri: Uri? = rememberImageUriPath(),
    onCropImageResult: (Bitmap?) -> Unit,
) {
    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }

    AnimatedVisibility(
        visible = isImageWithZoom.not(),
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
    ) {
        SubtitleCropImage(
            actionCropImage = actionCropImage,
            imageUri = imageUri,
            onCropImageResult = onCropImageResult,
            onCroppedImage = onCroppedImage,
        )
    }

    AnimatedVisibility(
        visible = isImageWithZoom,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .offset(offsetX, offsetY)
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, _, _ ->
                            offsetX += (pan.x / density).dp
                            offsetY += (pan.y / density).dp
                        },
                    )
                }
                .zoomable(rememberZoomableState()),
            contentScale = ContentScale.Crop,
            model = imageUri,
            contentDescription = null,
        )
    }
}

@Composable
private fun SubtitleCropImage(
    actionCropImage: ActionCropImage?,
    imageUri: Uri?,
    onCroppedImage: (ActionCropImage?) -> Unit,
    modifier: Modifier = Modifier,
    onCropImageResult: (Bitmap?) -> Unit,
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
            cornerShape = RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
        )
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setImageCropOptions(cropImageOptions)
    }
}

private fun CropImageView.cropRectDefault() {
    val rectRight = 250
    val rectBottom = 250
    val width = resources.displayMetrics.widthPixels
    cropRect = Rect(width - rectRight, 0, width, rectBottom)
}

@Preview(showBackground = true)
@Composable
private fun SubtitleImagePreview() {
    val resources = LocalContext.current.resources
    val cropImagePreview = crop_image_preview

    val imageUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(cropImagePreview))
        .appendPath(resources.getResourceTypeName(cropImagePreview))
        .appendPath(resources.getResourceEntryName(cropImagePreview))
        .build()

    SubtitleImage(
        isImageWithZoom = false,
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
