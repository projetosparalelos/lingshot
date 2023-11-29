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

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.common.util.findActivity
import com.lingshot.screenshot_presentation.R
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.model.aspectRatios
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import java.io.FileNotFoundException

@Composable
internal fun ScreenShotCropImage(
    isCrop: Boolean,
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap? = rememberImageBitmap(),
    onCropImageResult: (ImageBitmap?) -> Unit,
) {
    val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }

    val cropProperties by remember {
        mutableStateOf(
            CropDefaults.properties(
                contentScale = ContentScale.Fit,
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Rect,
                    RectCropShape(0, "Rect"),
                ),
                aspectRatio = aspectRatios[3].aspectRatio,
                handleSize = handleSize,
                maxZoom = 4f,
            ),
        )
    }
    val cropStyle by remember {
        mutableStateOf(
            CropDefaults.style(drawGrid = false, strokeWidth = 2.dp),
        )
    }

    imageBitmap?.let { image ->
        ImageCropper(
            modifier = modifier
                .fillMaxWidth(),
            imageBitmap = image,
            contentDescription = null,
            cropStyle = cropStyle,
            cropProperties = cropProperties,
            crop = isCrop,
            onCropStart = {},
            onCropSuccess = onCropImageResult,
        )
    }
}

@Composable
@Suppress("Deprecation")
private fun rememberImageBitmap(context: Context = LocalContext.current) = remember {
    val activity = context.findActivity()
    val intent = activity?.intent
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        uriToBitmap(context, intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java))
    } else {
        uriToBitmap(context, intent?.getParcelableExtra(Intent.EXTRA_STREAM))
    }?.asImageBitmap()
}

private fun uriToBitmap(context: Context, uri: Uri?): Bitmap? {
    uri?.let {
        try {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    return null
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotCropImagePreview() {
    val imageBitmap = ImageBitmap.imageResource(R.drawable.crop_image_preview)

    ScreenShotCropImage(
        isCrop = false,
        imageBitmap = imageBitmap,
        onCropImageResult = {},
    )
}
