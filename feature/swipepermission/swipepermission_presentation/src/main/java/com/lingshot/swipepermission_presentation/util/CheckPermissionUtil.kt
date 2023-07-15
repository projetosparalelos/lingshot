package com.lingshot.swipepermission_presentation.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat

fun allPermissionsGranted(context: Context) =
    (hasPermissions(context) && hasOverlayPermission(context))

private fun hasPermissions(context: Context) =
    PERMISSIONS.filter { it != WRITE_EXTERNAL_STORAGE }.all {
        ActivityCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED
    }

fun hasOverlayPermission(context: Context) =
    Settings.canDrawOverlays(context)

val PERMISSIONS = listOf(
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        READ_MEDIA_IMAGES
    } else {
        READ_EXTERNAL_STORAGE
    },
    WRITE_EXTERNAL_STORAGE
)
