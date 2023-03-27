@file:Suppress("Deprecation")

package com.teachmeprint.language.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.teachmeprint.language.R
import com.teachmeprint.language.TeachMePrintApplication.Companion.CHANNEL_ID
import com.teachmeprint.language.core.helper.ScreenCaptureManager
import com.teachmeprint.language.core.helper.ScreenShotDetection
import com.teachmeprint.language.core.util.fadeAnimation
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotActivity
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotFloatingWindow
import org.koin.android.ext.android.inject

class ScreenShotService: LifecycleService(), ScreenShotDetection.ScreenshotDetectionListener {

    private val screenShotFloatingWindow: ScreenShotFloatingWindow by inject()

    private val screenshotDetection = ScreenShotDetection(this)
    private val screenCaptureManager = ScreenCaptureManager()
    private var numScreenShotsTaken = 1

    override fun onCreate() {
        super.onCreate()
        screenshotDetection.startScreenshotDetection()
        screenShotFloatingWindow.onClickFloatingButton(lifecycleScope) {
            screenCaptureManager.captureScreenshot()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE) {
            stopSelf()
        }
        setupNotificationForeground()
        intent.sendIntentScreenCapture()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun Intent?.sendIntentScreenCapture() {
        val data: Intent? = this?.getParcelableExtra(SCREEN_CAPTURE_DATA)
        data?.let { screenCaptureManager.startCapture(AppCompatActivity.RESULT_OK, it) }
    }

    private fun setupNotificationForeground() {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.text_notification_title_display_display_tuned_on))
            .setContentText(getString(R.string.text_notification_message_display_reading_is_ready_to_use))
            .addAction(0, getString(R.string.text_notification_button_display_turn_off), intentStopScreenShot())
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

    private fun setupNotificationClearScreenshot() {
        numScreenShotsTaken++
        if (numScreenShotsTaken == NUM_SCREENSHOTS_TAKEN_MAX) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.text_notification_message_clean_up_gallery))
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
        screenShotFloatingWindow.close()
        numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
        screenshotDetection.stopScreenshotDetection()
        screenCaptureManager.stopCapture()
    }

    companion object {
        const val EXTRA_PATH_SCREEN_SHOT = "EXTRA_PATH_SCREEN_SHOT"
        private const val SCREEN_CAPTURE_DATA = "SCREEN_CAPTURE_DATA"
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val NOTIFICATION_FOREGROUND_ID = 1
        private const val NOTIFICATION_CLEAR_SCREEN_SHOT_ID = 2
        private const val NUM_SCREENSHOTS_TAKEN_INITIAL = 0
        private const val NUM_SCREENSHOTS_TAKEN_MAX = 10

        fun getStartIntent(context: Context?, data: Intent?): Intent {
           return Intent(context, ScreenShotService::class.java).apply {
               putExtra(SCREEN_CAPTURE_DATA, data)
            }
        }
    }
}