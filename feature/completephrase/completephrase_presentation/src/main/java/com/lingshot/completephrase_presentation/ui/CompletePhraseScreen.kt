@file:OptIn(ExperimentalFoundationApi::class)

package com.lingshot.completephrase_presentation.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseIndicatorPage
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseTextFieldCard
import com.lingshot.completephrase_presentation.ui.component.CompletePhraseTranslateCard
import com.lingshot.designsystem.component.LingshotLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun CompletePhraseScreenRoute() {
    CompletePhraseScreen()
}

@Composable
private fun CompletePhraseScreen() {
    val pagerState = rememberPagerState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val listPhrases = 10
    val currentPage = (pagerState.currentPage + 1)
    var enableVoice by remember { mutableStateOf(true) }
    LingshotLayout(
        title = "Complete phrase",
        onClickNavigation = {}
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                CompletePhraseIndicatorPage(currentPage, listPhrases)
                HorizontalPager(
                    pageCount = listPhrases,
                    state = pagerState,
                    userScrollEnabled = false
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CompletePhraseTextFieldCard(enableVoice)
                        CompletePhraseTranslateCard()
                    }
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(currentPage)
                    }
                    enableVoice = true
                }
            ) {
                Icon(imageVector = Icons.Default.SkipNext, contentDescription = null)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        delay(5000)
        enableVoice = false
        if (pagerState.currentPage != 0 && scrollState.value != 0) {
            scrollState.animateScrollTo(0)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseScreenPreview() {
    CompletePhraseScreen()
}
