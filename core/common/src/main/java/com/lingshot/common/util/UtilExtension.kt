package com.lingshot.common.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.lingshot.common.R

fun Context.fadeAnimation(): Bundle? =
    ActivityOptions.makeCustomAnimation(
        this,
        R.anim.fade_in,
        R.anim.fade_out
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
            }
        )
}

fun String.limitCharactersWithEllipsize(limit: Int): String {
    return if (length >= limit) {
        val stringBuilder = StringBuilder(substring(0, limit))
        stringBuilder.append("...")
        stringBuilder.toString()
    } else {
        this
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
