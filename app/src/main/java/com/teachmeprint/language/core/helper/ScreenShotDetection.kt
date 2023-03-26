@file:Suppress("Range", "Unused")

package com.teachmeprint.language.core.helper

import android.Manifest
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
import com.teachmeprint.language.TeachMePrintApplication
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import timber.log.Timber
import java.lang.ref.WeakReference

class ScreenShotDetection(
    private val reference: WeakReference<TeachMePrintApplication>,
    private val listener: ScreenshotDetectionListener
) {

    private var job: Job? = null

    constructor(
        listener: ScreenshotDetectionListener
    ) : this(WeakReference(TeachMePrintApplication.applicationContext()), listener)

    constructor(
        onScreenCaptured: (path: String) -> Unit
    ) : this(
        WeakReference(TeachMePrintApplication.applicationContext()),
        object : ScreenshotDetectionListener {
            override fun onScreenCaptured(path: String) {
                onScreenCaptured(path)
            }
        }
    )

    @OptIn(FlowPreview::class)
    fun startScreenshotDetection() {
        job = CoroutineScope(Dispatchers.Main).launch {
            createContentObserverFlow()
                .debounce(500)
                .collect { uri ->
                    reference.get()?.let { activity ->
                        onContentChanged(activity, uri)
                    }
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
        reference.get()
            ?.contentResolver
            ?.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver
            )
        awaitClose {
            reference.get()
                ?.contentResolver
                ?.unregisterContentObserver(contentObserver)
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
        return (screenshotDirectory != null &&
                lowercasePath?.contains(screenshotDirectory) == true) ||
                lowercasePath?.contains(CONTAIN_SCREEN_SHOT) == true
    }

    private fun getPublicScreenshotDirectoryName() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS).name
    } else null

    private fun getFilePathFromContentResolver(context: Context, uri: Uri): String? {
        try {
            context.contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
                ),
                null,
                null,
                null
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
        return reference.get()?.let { activity ->
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } ?: run {
            false
        }
    }

    interface ScreenshotDetectionListener {
        fun onScreenCaptured(path: String)
    }

    companion object {
        private const val CONTAIN_SCREEN_SHOT = "screenshot"
    }
}