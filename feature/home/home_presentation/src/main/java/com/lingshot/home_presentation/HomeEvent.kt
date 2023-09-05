package com.lingshot.home_presentation

sealed class HomeEvent {

    object ToggleExpandDropdownMenuSignOut : HomeEvent()

    object ToggleSetGoalsDialog : HomeEvent()

    data class SaveGoals(val day: Int) : HomeEvent()

    data class SelectedGoalDays(val day: Int) : HomeEvent()

    data class SignOut(val block: () -> Unit) : HomeEvent()
}
