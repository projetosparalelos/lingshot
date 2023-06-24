package com.teachmeprint.swipepermission_presentation

import android.content.Intent

sealed class SwipePermissionEvent {
    data class SignInWithIntent(val intent: Intent?) : SwipePermissionEvent()
}