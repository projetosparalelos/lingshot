package com.teachmeprint.language.core.util

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.google.android.material.R.id.snackbar_action
import com.google.android.material.snackbar.Snackbar
import com.teachmeprint.language.R

fun Activity.fadeAnimation() =
    overridePendingTransition(
        R.anim.fade_in,
        R.anim.fade_out);

fun View.snackBarAlert(
    @StringRes text: Int,
    @StringRes textAction: Int = R.string.text_button_action_close_snack_bar,
    isActionVisible: Boolean = false, onAction: () -> Unit = {}
) =
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).also { snackBar ->
        snackBar.setAnchorView(this)
            .setAction(textAction) { onAction.invoke() }
            .show()

        (snackBar.view.findViewById(snackbar_action) as TextView).apply {
            isAllCaps = false
            isVisible = isActionVisible
        }
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