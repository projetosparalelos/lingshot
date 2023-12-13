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
package com.lingshot.common.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Html
import android.view.View
import com.lingshot.common.R

fun Context.fadeAnimation(): Bundle? =
    ActivityOptions.makeCustomAnimation(
        this,
        R.anim.fade_in,
        R.anim.fade_out,
    ).toBundle()

fun View.isViewOverlapping(other: View, deltaX: Int = 0, deltaY: Int = 0): Boolean {
    val thisXY = IntArray(2).apply { getLocationOnScreen(this) }
    val otherXY = IntArray(2).apply {
        other.getLocationOnScreen(this)
        this[0] += deltaX
        this[1] += deltaY
    }
    return thisXY.let { Rect(it[0], it[1], it[0] + width, it[1] + height) }
        .intersect(
            otherXY.let {
                Rect(it[0], it[1], it[0] + other.width, it[1] + other.height)
            },
        )
}

fun String?.formatText(): String {
    return toString()
        .replace("\n", " ")
        .lowercase()
        .replaceFirstChar { it.uppercase() }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun String.decodeHtmlString(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
}

@Suppress("ReturnCount")
fun Context.isOnline(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
