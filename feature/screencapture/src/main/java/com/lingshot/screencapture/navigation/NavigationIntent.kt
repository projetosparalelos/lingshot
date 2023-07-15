package com.lingshot.screencapture.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lingshot.common.util.fadeAnimation

object NavigationIntent {
    private const val SCREEN_SHOT_ACTIVITY_PATH = "com.lingshot.screenshot_presentation.ui.ScreenShotActivity"

    fun launchScreenShotActivity(context: Context, uri: Uri?) {
        Intent(context, Class.forName(SCREEN_SHOT_ACTIVITY_PATH)).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it, context.fadeAnimation())
        }
    }
}
