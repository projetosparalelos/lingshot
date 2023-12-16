/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.subtitle_presentation.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.common.helper.onError
import com.lingshot.common.helper.onSuccess
import com.lingshot.common.util.decodeHtmlString
import com.lingshot.designsystem.theme.md_theme_dark_onSecondary
import com.lingshot.designsystem.theme.md_theme_dark_onSecondaryContainer
import com.lingshot.designsystem.theme.md_theme_dark_secondary
import com.lingshot.designsystem.theme.md_theme_dark_secondaryContainer
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.Status.Success
import com.lingshot.domain.model.statusDefault
import com.lingshot.subtitle_presentation.R
import com.lingshot.subtitle_presentation.Subtitle
import es.dmoral.toasty.Toasty.error
import kotlinx.coroutines.delay

@Composable
internal fun SubtitleBottomSheet(
    status: Status<List<Subtitle>>,
    countPhraseSelected: Int,
    onFetchTextRecognizerSelect: () -> Unit,
    onFetchTextTranslate: () -> Unit,
    onFocusImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContainerColor = md_theme_dark_onSecondary,
        sheetContentColor = md_theme_dark_secondary,
        sheetPeekHeight = if (status is Success) {
            48.dp
        } else {
            80.dp
        },
        sheetSwipeEnabled = (status is Success),
        sheetDragHandle = {
            if (status is Success) {
                BottomSheetDefaults.DragHandle(color = md_theme_dark_secondary)
            } else {
                SubtitleNavigationItem(
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                    countPhraseSelected = countPhraseSelected,
                    onFetchTextRecognizerSelect = onFetchTextRecognizerSelect,
                    onFetchTextTranslate = onFetchTextTranslate,
                    onFocusImage = onFocusImage,
                )
            }
        },
        sheetContent = {
            Column(modifier = Modifier.heightIn(min = 80.dp, max = 200.dp)) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = md_theme_dark_secondaryContainer,
                )
                status.onSuccess { listSubtitle ->
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        itemsIndexed(listSubtitle) { position, item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .then(
                                        if (position == listSubtitle.size - 1) {
                                            Modifier.padding(top = 16.dp, bottom = 16.dp)
                                        } else {
                                            Modifier.padding(top = 16.dp)
                                        },
                                    ),
                            ) {
                                SpeechBubble(text = item.text, imageBitmap = item.bitmap)
                            }
                        }
                    }
                }.onError { errorMessage ->
                    error(context, errorMessage).show()
                }
            }
        },
    ) {}

    LaunchedEffect(status) {
        delay(500)
        if ((status is Success)) {
            scaffoldState.bottomSheetState.expand()
        }
    }
}

@Composable
private fun SpeechBubble(text: String, imageBitmap: Bitmap?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(topEnd = 30.dp, bottomStart = 30.dp),
            )
            .background(md_theme_dark_secondaryContainer)
            .padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            imageBitmap?.let {
                Image(
                    modifier = Modifier
                        .widthIn(max = 150.dp)
                        .heightIn(max = 150.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(
                                topStart = 48f,
                                topEnd = 48f,
                                bottomStart = 0f,
                                bottomEnd = 48f,
                            ),
                        ),
                    contentScale = ContentScale.Crop,
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text.decodeHtmlString(),
                fontWeight = FontWeight.Bold,
                color = md_theme_dark_onSecondaryContainer,
                fontFamily = Font(R.font.comic_sans).toFontFamily(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SubtitleBottomSheetPreview() {
    SubtitleBottomSheet(
        status = statusDefault(),
        countPhraseSelected = 0,
        onFetchTextRecognizerSelect = {},
        onFetchTextTranslate = {},
        onFocusImage = {},
    )
}
