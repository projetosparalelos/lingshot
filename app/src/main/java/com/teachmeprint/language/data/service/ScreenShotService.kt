@file:Suppress("Deprecation", "InflateParams", "Warning")

package com.teachmeprint.language.data.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat.TRANSLUCENT
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.view.WindowManager.LayoutParams.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.teachmeprint.language.R
import com.teachmeprint.language.TeachMePrintApplication.Companion.CHANNEL_ID
import com.teachmeprint.language.core.helper.ScreenCaptureManager
import com.teachmeprint.language.core.helper.ScreenShotDetection
import com.teachmeprint.language.core.util.fadeAnimation
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScreenShotService: LifecycleService(), ScreenShotDetection.ScreenshotDetectionListener {

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private val rootView by lazy {
        LayoutInflater.from(this).inflate(R.layout.floating_window_layout, null)
    }
    private lateinit var windowParams: WindowManager.LayoutParams

    private val screenshotDetection = ScreenShotDetection(this)
    private var screenCaptureManager: ScreenCaptureManager = ScreenCaptureManager()
    private var numScreenShotsTaken = 1

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    private val floatingButtonScreenShot by lazy {
        rootView.findViewById<ImageButton>(R.id.buttonService)
    }

    override fun onCreate() {
        super.onCreate()
        setupWindowParams()

        start()
        screenshotDetection.startScreenshotDetection()
        onTouchMoveFloatingButton()
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

    private fun setupWindowParams() {
       windowParams = WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TYPE_APPLICATION_OVERLAY
            } else {
               TYPE_PHONE
            },
           FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_NO_LIMITS or FLAG_NOT_TOUCH_MODAL,
            TRANSLUCENT
        ).apply {
           gravity = Gravity.TOP or Gravity.START
           x = 0
           y = 0
       }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchMoveFloatingButton() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        floatingButtonScreenShot.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = windowParams.x
                    initialY = windowParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    windowParams.x = (initialX + event.rawX - initialTouchX).toInt()
                    windowParams.y = (initialY + event.rawY - initialTouchY).toInt()

                    if (windowParams.x < 0) windowParams.x = 0
                    if (windowParams.y < 0) windowParams.y = 0
                    if (windowParams.x > screenWidth - rootView.width) windowParams.x = screenWidth - rootView.width
                    if (windowParams.y > screenHeight - rootView.height) windowParams.y = screenHeight - rootView.height

                    windowManager.updateViewLayout(rootView, windowParams)
                }
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    screenCaptureManager.captureScreenshot()

                }
            }
            false
        }
        floatingButtonScreenShot.onClickFloatingButton()
    }

    private fun ImageButton.onClickFloatingButton() {
        setOnClickListener {
            isVisible = false
            lifecycleScope.launch {
                delay(50L)
                withContext(Dispatchers.IO) {
                    screenCaptureManager.captureScreenshot()
                }
                isVisible = true
            }
        }
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
        close()
        numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
        screenshotDetection.stopScreenshotDetection()
        screenCaptureManager.stopCapture()
    }

    private fun start() = runCatching {
        windowManager.addView(rootView, windowParams)
    }.getOrNull()

    private fun close() = runCatching {
        windowManager.removeView(rootView)
    }.getOrNull()

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