package com.teachmeprint.language.swipeable_permission.util

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.teachmeprint.screencapture.service.ScreenShotService

fun allPermissionsGranted(context: Context) =
    (hasPermissions(context) && hasOverlayPermission(context))

private fun hasPermissions(context: Context) =
    PERMISSIONS.filter { it != WRITE_EXTERNAL_STORAGE }.all {
        ActivityCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED
    }

fun hasOverlayPermission(context: Context) =
    Settings.canDrawOverlays(context)

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

val PERMISSIONS = listOf(
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    },
    WRITE_EXTERNAL_STORAGE
)