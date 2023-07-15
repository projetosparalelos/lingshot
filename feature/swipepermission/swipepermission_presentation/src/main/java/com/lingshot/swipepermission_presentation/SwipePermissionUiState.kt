package com.lingshot.swipepermission_presentation

import com.lingshot.swipepermission_presentation.ui.SwipePermissionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SwipePermissionUiState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val swipePermissionItemList: ImmutableList<SwipePermissionItem> =
        enumValues<SwipePermissionItem>().toList().toImmutableList()
)
