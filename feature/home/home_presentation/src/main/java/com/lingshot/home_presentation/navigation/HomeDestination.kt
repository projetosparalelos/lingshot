package com.lingshot.home_presentation.navigation

data class HomeDestination(
    val onNavigateToCompletePhrase: (String) -> Unit = {},
    val onSignOut: () -> Unit = {}
)
