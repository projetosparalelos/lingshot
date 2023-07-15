package com.lingshot.screencapture.util

import android.app.ActivityManager
import android.content.Context
import com.lingshot.screencapture.service.ScreenShotService

@Suppress("DEPRECATION")
fun isServiceRunning(context: Context): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
        if (ScreenShotService::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}
