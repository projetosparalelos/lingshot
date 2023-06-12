package com.teachmeprint.screencapture.helper

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.teachmeprint.screencapture.ScreenCaptureFloatingWindow
import javax.inject.Inject

class ScreenCaptureFloatingWindowLifecycle @Inject constructor(
    private val screenCaptureFloatingWindow: ScreenCaptureFloatingWindow
) :
    DefaultLifecycleObserver {

    operator fun invoke(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        screenCaptureFloatingWindow.showOrHide(false)
    }

    override fun onPause(owner: LifecycleOwner) {
        screenCaptureFloatingWindow.showOrHide()
    }
}
