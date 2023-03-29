@file:Suppress("Deprecation")

package com.teachmeprint.language.data.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.teachmeprint.language.R
import com.teachmeprint.language.TeachMePrintApplication.Companion.CHANNEL_ID
import com.teachmeprint.language.core.helper.NotificationClearScreenshot
import com.teachmeprint.language.core.helper.ScreenCaptureManager
import com.teachmeprint.language.core.helper.ScreenShotDetection
import com.teachmeprint.language.core.util.NavigationIntentUtil
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotFloatingWindow
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ScreenShotService: LifecycleService(), ScreenShotDetection.ScreenshotDetectionListener {

    private val screenShotFloatingWindow: ScreenShotFloatingWindow by inject()
    private val notificationClearScreenshot: NotificationClearScreenshot by inject()
    private val screenshotDetection: ScreenShotDetection by inject {  parametersOf(this)  }
    private val screenCaptureManager: ScreenCaptureManager by inject()

    private var isOrientationPortrait: Boolean = true

    override fun onCreate() {
        super.onCreate()
        screenShotFloatingWindow.start()
        screenshotDetection.startScreenshotDetection()
        screenShotFloatingWindow.onFloating(lifecycleScope,
            onScreenShot = {
                screenCaptureManager.captureScreenshot()
            },
            onStopService = {
                stopSelf()
            })
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)

        isOrientationPortrait = if (configuration.orientation == ORIENTATION_PORTRAIT) {
            screenShotFloatingWindow.showOrHide()
            true
        } else {
            screenShotFloatingWindow.showOrHide(false)
            false
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

    override fun onScreenCaptured(path: String) {
        if (isOrientationPortrait) {
            notificationClearScreenshot.start()
            NavigationIntentUtil.launchScreenShotActivity(this, path)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        screenShotFloatingWindow.close()
        notificationClearScreenshot.clear()
        screenshotDetection.stopScreenshotDetection()
        screenCaptureManager.stopCapture()
    }

    companion object {
        const val EXTRA_PATH_SCREEN_SHOT = "EXTRA_PATH_SCREEN_SHOT"
        private const val SCREEN_CAPTURE_DATA = "SCREEN_CAPTURE_DATA"
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val NOTIFICATION_FOREGROUND_ID = 1

        fun getStartIntent(context: Context?, data: Intent?): Intent {
           return Intent(context, ScreenShotService::class.java).apply {
               putExtra(SCREEN_CAPTURE_DATA, data)
            }
        }
    }
}