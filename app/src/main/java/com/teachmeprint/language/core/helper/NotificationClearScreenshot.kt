package com.teachmeprint.language.core.helper

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.teachmeprint.language.R
import com.teachmeprint.language.TeachMePrintApplication

class NotificationClearScreenshot(private val context: Context) {
    private var numScreenShotsTaken = 1

    fun start() {
        numScreenShotsTaken++
        if (numScreenShotsTaken == NUM_SCREENSHOTS_TAKEN_MAX) {
            val notification = NotificationCompat.Builder(context, TeachMePrintApplication.CHANNEL_ID)
                .setContentTitle(context.getString(R.string.text_notification_message_clean_up_gallery))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_delete_24)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_CLEAR_SCREEN_SHOT_ID, notification)

            numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
        }
    }

    fun clear() {
        numScreenShotsTaken = NUM_SCREENSHOTS_TAKEN_INITIAL
    }

    companion object {
        private const val NOTIFICATION_CLEAR_SCREEN_SHOT_ID = 2
        private const val NUM_SCREENSHOTS_TAKEN_INITIAL = 0
        private const val NUM_SCREENSHOTS_TAKEN_MAX = 10
    }
}