package com.teachmeprint.language.swipeable_permission

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SwipePermissionUiState(
    val swipePermissionItemList: ImmutableList<SwipePermissionItem> =
        enumValues<SwipePermissionItem>().toList().toImmutableList()
)