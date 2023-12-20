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

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lingshot.designsystem.component.ActionCropImage
import com.lingshot.designsystem.component.LingshotCropImage
import com.lingshot.designsystem.component.rememberImageUriPath
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

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
        LingshotCropImage(
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
