package com.lingshot.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
fun LingshotWebView(url: String, modifier: Modifier = Modifier) {
    val state = rememberWebViewState(url)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        WebView(
            modifier = Modifier.fillMaxSize(),
            state = state,
            captureBackPresses = false
        )
        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}
