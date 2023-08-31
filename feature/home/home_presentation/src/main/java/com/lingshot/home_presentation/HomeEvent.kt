package com.lingshot.home_presentation

sealed class HomeEvent {

    object ToggleExpandDropdownMenuSignOut : HomeEvent()

    data class SignOut(val block: () -> Unit) : HomeEvent()
}
