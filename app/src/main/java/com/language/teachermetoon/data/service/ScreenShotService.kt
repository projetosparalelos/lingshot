@file:Suppress("Deprecation", "InflateParams", "Warning")

package com.language.teachermetoon.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.PixelFormat.TRANSPARENT
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import androidx.core.app.NotificationCompat
import com.language.teachermetoon.MainActivity
import com.language.teachermetoon.MyApplication.Companion.CHANNEL_ID
import com.language.teachermetoon.R
import com.language.teachermetoon.core.helper.ScreenShotDetection
import com.language.teachermetoon.feature.screenshot.presentation.ui.ScreenShotActivity
import com.language.teachermetoon.core.util.fadeAnimation

class ScreenShotService: Service(), ScreenShotDetection.ScreenshotDetectionListener {

    private val windowManager by lazy {
       getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val rootView by lazy { View(this) }

    private val windowParams = WindowManager.LayoutParams(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TYPE_APPLICATION_OVERLAY
        } else {
            TYPE_PHONE
        },
    ).apply {
        width = 0
        height = 0
        flags = FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_IN_SCREEN
        format = TRANSPARENT
    }

    private val screenshotDetection = ScreenShotDetection(this)

    private var numScreenShotsTaken = 1

    override fun onCreate() {
        super.onCreate()
        start()
        screenshotDetection.startScreenshotDetection()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE) {
            stopSelf()
            backToMainActivity()
        }

        setupNotificationForeground()
        return START_STICKY
    }

    private fun setupNotificationForeground() {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reading activated")
            .setContentText("Click here to interrupt.")
            .setContentIntent(intentStopScreenShot())
            .setSmallIcon(R.drawable.ic_translate_24).run {
                startForeground(NOTIFICATION_FOREGROUND_ID, build())
            }
    }

    private fun intentStopScreenShot() =
        Intent(this, ScreenShotService::class.java).run {
            action = STOP_SERVICE
            PendingIntent.getService(this@ScreenShotService,
                0, this, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }

    private fun backToMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
        }.also {
            setupNotificationClearScreenshot()
            startActivity(it, fadeAnimation())
        }
    }

    private fun setupNotificationClearScreenshot() {
        numScreenShotsTaken++
        if (numScreenShotsTaken == NUM_SCREENSHOTS_TAKEN_MAX) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Clean up your screenshots in the gallery.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_delete_24)
                .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_CLEAR_SCREEN_SHOT_ID, notification)

            numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
        }
    }

    override fun onScreenCaptured(path: String) {
        setupNotificationClearScreenshot()
        Intent(this, ScreenShotActivity::class.java).apply {
            putExtra(EXTRA_PATH_SCREEN_SHOT, path)
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also {
            startActivity(it, fadeAnimation())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
        numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
        screenshotDetection.stopScreenshotDetection()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun start() = runCatching {
        windowManager.addView(rootView, windowParams)
    }.getOrNull()

    private fun close() = runCatching {
        windowManager.removeView(rootView)
    }.getOrNull()

    companion object {
        const val EXTRA_PATH_SCREEN_SHOT = "EXTRA_PATH_SCREEN_SHOT"
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val NOTIFICATION_FOREGROUND_ID = 1
        private const val NOTIFICATION_CLEAR_SCREEN_SHOT_ID = 2
        private const val NUM_SCREENSHOTS_TAKEN_INITIAL = 0
        private const val NUM_SCREENSHOTS_TAKEN_MAX = 10
    }
}