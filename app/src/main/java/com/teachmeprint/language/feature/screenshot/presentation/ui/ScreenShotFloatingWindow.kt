@file:Suppress("Deprecation", "InflateParams", "ClickableViewAccessibility")

package com.teachmeprint.language.feature.screenshot.presentation.ui

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.teachmeprint.language.R
import kotlinx.coroutines.*

class ScreenShotFloatingWindow(context: Context) {

    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private val rootView by lazy {
        LayoutInflater.from(context).inflate(R.layout.floating_window_layout, null)
    }

    private lateinit var windowParams: WindowManager.LayoutParams

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private val touchSensitivity = 20

    private val floatingButtonScreenShot by lazy {
        rootView.findViewById<ImageButton>(R.id.buttonService)
    }

    init {
        setupWindowParams()
        onTouchMoveFloatingButton()
    }

    fun onClickFloatingButton(coroutineScope: CoroutineScope, block: () -> Unit) =
        with(floatingButtonScreenShot) {
            setOnClickListener {
                showOrHide(false)
                coroutineScope.launch {
                    delay(50L)
                    withContext(Dispatchers.IO) {
                        block.invoke()
                    }
                    showOrHide()
                }
            }
        }

    private fun onTouchMoveFloatingButton() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        floatingButtonScreenShot.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = windowParams.x
                    initialY = windowParams.y
                    initialTouchX = event.rawX + touchSensitivity
                    initialTouchY = event.rawY + touchSensitivity
                }
                MotionEvent.ACTION_MOVE -> {
                    windowParams.x = (initialX + event.rawX - initialTouchX).toInt()
                    windowParams.y = (initialY + event.rawY - initialTouchY).toInt()

                    if (windowParams.x < 0) windowParams.x = 0
                    if (windowParams.y < 0) windowParams.y = 0
                    if (windowParams.x > screenWidth - rootView.width) windowParams.x = screenWidth - rootView.width
                    if (windowParams.y > screenHeight - rootView.height) windowParams.y = screenHeight - rootView.height

                    windowManager.updateViewLayout(rootView, windowParams)
                }
            }
            false
        }
    }

    private fun setupWindowParams() {
        windowParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
        }
    }

    fun showOrHide(isVisible: Boolean = true) {
        rootView.isVisible = isVisible
    }

    fun start() = runCatching {
        windowManager.addView(rootView, windowParams)
    }.getOrNull()

    fun close() = runCatching {
        windowManager.removeView(rootView)
    }.getOrNull()
}