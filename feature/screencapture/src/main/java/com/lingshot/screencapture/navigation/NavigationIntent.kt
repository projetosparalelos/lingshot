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
