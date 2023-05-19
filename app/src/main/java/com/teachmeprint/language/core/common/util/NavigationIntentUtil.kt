package com.teachmeprint.language.core.common.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.teachmeprint.language.screen_shot.presentation.ui.ScreenShotActivity

object NavigationIntentUtil {
    fun launchScreenShotActivity(context: Context, uri: Uri?) {
        Intent(context, ScreenShotActivity::class.java).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it, context.fadeAnimation())
        }
    }
}