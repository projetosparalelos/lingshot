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

package com.lingshot.screencapture.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
import android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import com.lingshot.screencapture.navigation.NavigationIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class ScreenCaptureManager @Inject constructor(
    private val context: Context,
) {

    private val mediaProjectionManager: MediaProjectionManager =
        context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    private val displayMetrics by lazy { context.resources.displayMetrics }

    @SuppressLint("WrongConstant")
    fun startCapture(resultCode: Int, data: Intent) {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
        imageReader = ImageReader.newInstance(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            android.graphics.PixelFormat.RGBA_8888,
            2,
        )
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_NAME_DISPLAY,
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            displayMetrics.densityDpi,
            VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or VIRTUAL_DISPLAY_FLAG_PUBLIC,
            imageReader?.surface,
            null,
            null,
        )
    }

    fun stopCapture() {
        virtualDisplay?.release()
        imageReader?.setOnImageAvailableListener(null, null)
        mediaProjection?.stop()
        mediaProjection = null
    }

    fun captureScreenshot(coroutineScope: CoroutineScope): Bitmap? {
        val image: Image? = imageReader?.acquireLatestImage()
        val bitmap: Bitmap? = image?.let { imageToBitmap(it) }
        image?.close()
        val cropBitmap: Bitmap? = bitmap?.let { cropBitmap(it) }
        saveBitmap(cropBitmap, coroutineScope)
        return bitmap
    }

    private fun imageToBitmap(image: Image): Bitmap? {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * displayMetrics.widthPixels

        val bitmap = Bitmap.createBitmap(
            displayMetrics.widthPixels + rowPadding / pixelStride,
            displayMetrics.heightPixels,
            Bitmap.Config.ARGB_8888,
        )
        bitmap.copyPixelsFromBuffer(buffer)
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
        )
    }

    private fun cropBitmap(bitmap: Bitmap): Bitmap? {
        val topHeight = cropPeace(28)
        val bottomHeight = cropPeace(46)
        val croppedBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height - topHeight - bottomHeight,
            Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(bitmap, 0f, (-topHeight).toFloat(), null)
        return croppedBitmap
    }

    private fun cropPeace(px: Int): Int {
        return (px * context.resources.displayMetrics.density).toInt()
    }

    private fun saveBitmap(bitmap: Bitmap?, coroutineScope: CoroutineScope) {
        if (bitmap == null) return
        val file = fileScreenShot()

        coroutineScope.launch {
            if (file.exists()) {
                file.delete()
            }

            withContext(Dispatchers.IO) {
                val newFile = fileScreenShot()
                try {
                    val fos = FileOutputStream(newFile)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()

                    if (newFile.exists() &&
                        (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
                    ) {
                        context.let {
                            NavigationIntent.launchScreenShotActivity(
                                it,
                                Uri.fromFile(newFile),
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fileScreenShot(): File {
        return File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES), FILE_NAME)
    }

    fun deleteScreenShot() {
        if (fileScreenShot().exists()) {
            fileScreenShot().delete()
        }
    }

    companion object {
        const val FILE_NAME = "Lingshot_Screenshot.jpg"
        private const val VIRTUAL_NAME_DISPLAY = "ScreenCapture"
    }
}
