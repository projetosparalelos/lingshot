package com.lingshot.home_presentation.navigation

data class HomeDestination(
    val onNavigateToCompletePhrase: (String, String, String) -> Unit = { _, _, _ -> },
    val onSignOut: () -> Unit = {}
)
