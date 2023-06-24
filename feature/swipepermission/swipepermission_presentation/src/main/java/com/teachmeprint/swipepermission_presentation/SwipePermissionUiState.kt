package com.teachmeprint.swipepermission_presentation

import com.teachmeprint.swipepermission_presentation.ui.SwipePermissionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SwipePermissionUiState(
    val isSignInSuccessful: Boolean = false,
    val swipePermissionItemList: ImmutableList<SwipePermissionItem> =
        enumValues<SwipePermissionItem>().toList().toImmutableList()
)