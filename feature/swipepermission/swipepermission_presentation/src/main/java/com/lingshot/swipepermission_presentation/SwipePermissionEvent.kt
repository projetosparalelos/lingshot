package com.lingshot.swipepermission_presentation

import android.content.Intent

sealed class SwipePermissionEvent {
    object ClearState : SwipePermissionEvent()
    data class SignInWithIntent(val intent: Intent?) : SwipePermissionEvent()
}
