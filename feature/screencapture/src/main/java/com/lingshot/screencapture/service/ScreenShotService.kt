@file:Suppress("Deprecation")

package com.lingshot.screencapture.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.lingshot.common.CommonConstant.CHANNEL_ID
import com.lingshot.screencapture.R
import com.lingshot.screencapture.ScreenCaptureFloatingWindow
import com.lingshot.screencapture.helper.ScreenCaptureManager
import com.lingshot.screencapture.helper.ScreenShotDetection
import com.lingshot.screencapture.navigation.NavigationIntent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScreenShotService : LifecycleService(), ScreenShotDetection.ScreenshotDetectionListener {

    @Inject
    lateinit var screenCaptureFloatingWindow: ScreenCaptureFloatingWindow

    @Inject
    lateinit var screenshotDetectionFactory: ScreenShotDetection.Factory
    private val screenshotDetection by lazy { screenshotDetectionFactory.create(this) }

    @Inject
    lateinit var screenCaptureManager: ScreenCaptureManager

    private var isOrientationPortrait: Boolean = true

    override fun onCreate() {
        super.onCreate()
        screenCaptureFloatingWindow.start()
        screenshotDetection.startScreenshotDetection()
        screenCaptureFloatingWindow.onFloating(
            lifecycleScope,
            onScreenShot = {
                screenCaptureManager.captureScreenshot(lifecycleScope)
            }
        )
        lifecycleScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                delay(2.seconds)
            }
            screenCaptureFloatingWindow.onFloatingClose { stopSelf() }
        }
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)

        isOrientationPortrait = if (configuration.orientation == ORIENTATION_PORTRAIT) {
            screenCaptureFloatingWindow.showOrHide()
            true
        } else {
            screenCaptureFloatingWindow.showOrHide(false)
            false
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE) {
            screenCaptureManager.deleteScreenShot()
            stopSelf()
        }
        setupNotificationForeground()
        intent.sendIntentScreenCapture()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun Intent?.sendIntentScreenCapture() {
        val data: Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this?.getParcelableExtra(SCREEN_CAPTURE_DATA, Intent::class.java)
        } else {
            this?.getParcelableExtra(SCREEN_CAPTURE_DATA)
        }
        data?.let { screenCaptureManager.startCapture(RESULT_OK, it) }
    }

    private fun setupNotificationForeground() {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.text_notification_title_display_display_tuned_on))
            .setContentText(
                getString(R.string.text_notification_message_display_reading_is_ready_to_use)
            )
            .addAction(
                0,
                getString(R.string.text_notification_button_display_turn_off),
                intentStopScreenShot()
            )
            .setSmallIcon(R.drawable.ic_translate_24).run {
                startForeground(NOTIFICATION_FOREGROUND_ID, build())
            }
    }

    private fun intentStopScreenShot() =
        Intent(this, ScreenShotService::class.java).run {
            action = STOP_SERVICE
            PendingIntent.getService(
                this@ScreenShotService,
                0,
                this,
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
        }

    override fun onScreenCaptured(path: String) {
        if (isOrientationPortrait) {
            NavigationIntent.launchScreenShotActivity(this, Uri.fromFile(File(path)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        screenCaptureFloatingWindow.close()
        screenshotDetection.stopScreenshotDetection()
        screenCaptureManager.stopCapture()
        screenCaptureManager.deleteScreenShot()
    }

    companion object {
        private const val SCREEN_CAPTURE_DATA = "SCREEN_CAPTURE_DATA"
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val NOTIFICATION_FOREGROUND_ID = 1

        fun getStartIntent(context: Context?, data: Intent?): Intent {
            return Intent(context, ScreenShotService::class.java).apply {
                putExtra(SCREEN_CAPTURE_DATA, data)
            }
        }

        fun getStopIntent(context: Context): Intent {
            return Intent(context, ScreenShotService::class.java)
        }
    }
}
