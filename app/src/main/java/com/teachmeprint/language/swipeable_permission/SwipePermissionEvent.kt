package com.teachmeprint.language.swipeable_permission

sealed class SwipePermissionEvent {
    object HasOverlayPermission : SwipePermissionEvent()
}