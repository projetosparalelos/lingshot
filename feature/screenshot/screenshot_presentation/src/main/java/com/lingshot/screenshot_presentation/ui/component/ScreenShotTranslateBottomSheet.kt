@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.lingshot.screenshot_presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.common.helper.onError
import com.lingshot.common.helper.onLoading
import com.lingshot.common.helper.onSuccess
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.placeholder
import com.lingshot.designsystem.component.placeholder.shimmer
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusSuccess
import com.lingshot.screenshot_domain.model.LanguageTranslationDomain
import com.lingshot.screenshot_presentation.R

@Composable
internal fun ScreenShotTranslateBottomSheet(
    languageTranslationDomain: LanguageTranslationDomain,
    correctedOriginalTextStatus: Status<String>,
    onCorrectedOriginalText: (String) -> Unit,
    isPhraseSaved: Boolean,
    onCheckPhraseInLanguageCollection: (String) -> Unit,
    onSetPhraseDomain: (String, String) -> Unit,
    onToggleDictionaryFullScreenDialog: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var correctedOriginalText by remember { mutableStateOf(languageTranslationDomain.originalText) }
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.text_title_translate_bottom_sheet),
                    style = MaterialTheme.typography.titleLarge
                )
                ScreenShotButtonAddToList(
                    isPhraseSaved = isPhraseSaved,
                    isLoadingStatus = correctedOriginalTextStatus.isLoadingStatus,
                    onSetPhraseDomain = {
                        onSetPhraseDomain(
                            correctedOriginalText,
                            languageTranslationDomain.translatedText.toString()
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(42.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(12.dp),
                    imageVector = Icons.Default.Translate,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = languageTranslationDomain.translatedText.orEmpty(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ScreenShotCorrectedOriginalText(
                        correctedOriginalText = correctedOriginalText,
                        isLoadingStatus = correctedOriginalTextStatus.isLoadingStatus,
                        onToggleDictionaryFullScreenDialog = {
                            onToggleDictionaryFullScreenDialog(
                                languageTranslationDomain.dictionaryUrl(it)
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        correctedOriginalTextStatus
            .onLoading { correctedOriginalText = languageTranslationDomain.originalText }
            .onSuccess {
                correctedOriginalText = it
                onCheckPhraseInLanguageCollection(it)
            }
            .onError { correctedOriginalText = it }
    }

    LaunchedEffect(Unit) {
        onCorrectedOriginalText(languageTranslationDomain.originalText)
    }
}

@Composable
private fun ScreenShotButtonAddToList(
    isPhraseSaved: Boolean,
    isLoadingStatus: Boolean,
    onSetPhraseDomain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint = if (isPhraseSaved) {
        Color.Red
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    FilledTonalButton(
        modifier = modifier,
        enabled = !isLoadingStatus,
        onClick = onSetPhraseDomain
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Favorite,
                tint = iconTint,
                contentDescription = null
            )
            AnimatedVisibility(
                visible = isPhraseSaved,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.text_button_add_to_list_bottom_sheet)
                )
            }
        }
    }
}

@Composable
private fun ScreenShotCorrectedOriginalText(
    correctedOriginalText: String,
    isLoadingStatus: Boolean,
    onToggleDictionaryFullScreenDialog: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.SubdirectoryArrowRight,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        ScreenShotOpenDictionaryByWord(
            modifier = Modifier
                .placeholder(
                    visible = isLoadingStatus,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            text = correctedOriginalText,
            onToggleDictionaryFullScreenDialog = onToggleDictionaryFullScreenDialog
        )
    }
}

@Composable
fun ScreenShotOpenDictionaryByWord(
    text: String,
    modifier: Modifier = Modifier,
    onToggleDictionaryFullScreenDialog: (String) -> Unit
) {
    val words = text.split(" ")
    FlowRow(modifier = modifier) {
        words.forEach { word ->
            Text(
                text = word,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onToggleDictionaryFullScreenDialog(word) }
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotTranslateBottomSheetPreview() {
    ScreenShotTranslateBottomSheet(
        languageTranslationDomain = LanguageTranslationDomain(
            "Original",
            "Translated",
            "en",
            "pt"
        ),
        isPhraseSaved = false,
        correctedOriginalTextStatus = statusSuccess("Corrected original"),
        onCorrectedOriginalText = {},
        onCheckPhraseInLanguageCollection = {},
        onSetPhraseDomain = { _, _ -> },
        onToggleDictionaryFullScreenDialog = {},
        onDismiss = {}
    )
}
