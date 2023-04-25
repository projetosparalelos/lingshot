package com.teachmeprint.language.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotActivity
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotComposeActivity

object NavigationIntentUtil {
    fun launchScreenShotActivity(context: Context, uri: Uri?) {
        Intent(context, ScreenShotComposeActivity::class.java).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it, context.fadeAnimation())
        }
    }
}