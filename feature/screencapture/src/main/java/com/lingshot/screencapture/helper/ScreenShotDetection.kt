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
@file:Suppress("Range", "Unused")

package com.lingshot.screencapture.helper

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import timber.log.Timber

class ScreenShotDetection @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val listener: ScreenshotDetectionListener,
) {

    private var job: Job? = null

    @AssistedFactory
    interface Factory {
        fun create(listener: ScreenshotDetectionListener): ScreenShotDetection
    }

    @OptIn(FlowPreview::class)
    fun startScreenshotDetection() {
        job = CoroutineScope(Dispatchers.Main).launch {
            createContentObserverFlow()
                .debounce(500)
                .collect { uri ->
                    onContentChanged(context, uri)
                }
        }
    }

    fun stopScreenshotDetection() {
        job?.cancel()
    }

    private fun createContentObserverFlow() = channelFlow {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                uri?.let { _ ->
                    trySend(uri)
                }
            }
        }
        context.contentResolver?.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver,
        )
        awaitClose {
            context.contentResolver?.unregisterContentObserver(contentObserver)
        }
    }

    private fun onContentChanged(context: Context, uri: Uri) {
        if (isReadExternalStoragePermissionGranted()) {
            val path = getFilePathFromContentResolver(context, uri)

            path?.let { p ->
                if (isScreenshotPath(p)) {
                    onScreenCaptured(p)
                }
            }
        }
    }

    private fun onScreenCaptured(path: String) {
        listener.onScreenCaptured(path)
    }

    private fun isScreenshotPath(path: String?): Boolean {
        val lowercasePath = path?.lowercase()
        val screenshotDirectory = getPublicScreenshotDirectoryName()?.lowercase()
        return (
            screenshotDirectory != null &&
                lowercasePath?.contains(screenshotDirectory) == true
            ) ||
            lowercasePath?.contains(CONTAIN_SCREEN_SHOT) == true
    }

    private fun getPublicScreenshotDirectoryName() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS).name
        } else {
            null
        }

    private fun getFilePathFromContentResolver(context: Context, uri: Uri): String? {
        try {
            context.contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                ),
                null,
                null,
                null,
            )?.let { cursor ->
                cursor.moveToFirst()
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                cursor.close()
                return path
            }
        } catch (e: Exception) {
            Timber.w(e.message ?: "")
        }
        return null
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_IMAGES
        } else {
            READ_EXTERNAL_STORAGE
        }
        return context.let { activity ->
            ContextCompat.checkSelfPermission(
                activity,
                permission,
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    interface ScreenshotDetectionListener {
        fun onScreenCaptured(path: String)
    }

    companion object {
        private const val CONTAIN_SCREEN_SHOT = "screenshot"
    }
}
