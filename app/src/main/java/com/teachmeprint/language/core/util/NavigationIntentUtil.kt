package com.teachmeprint.language.core.util

import android.content.Context
import android.content.Intent
import com.teachmeprint.language.data.service.ScreenShotService
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotActivity

object NavigationIntentUtil {
    fun launchScreenShotActivity(context: Context, path: String) {
        Intent(context, ScreenShotActivity::class.java).apply {
            putExtra(ScreenShotService.EXTRA_PATH_SCREEN_SHOT, path)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it, context.fadeAnimation())
        }
    }
}