/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    PERMISSIONS.all {
        ActivityCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED
    }

fun hasOverlayPermission(context: Context) =
    Settings.canDrawOverlays(context)

val PERMISSIONS = listOf(
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        READ_MEDIA_IMAGES
    } else {
        READ_EXTERNAL_STORAGE
        WRITE_EXTERNAL_STORAGE
    },
)
