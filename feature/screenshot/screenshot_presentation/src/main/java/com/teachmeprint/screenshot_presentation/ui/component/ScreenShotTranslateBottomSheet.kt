@file:OptIn(ExperimentalMaterial3Api::class)

package com.teachmeprint.screenshot_presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.teachmeprint.common.helper.isLoadingStatus
import com.teachmeprint.common.helper.onError
import com.teachmeprint.common.helper.onLoading
import com.teachmeprint.common.helper.onSuccess
import com.teachmeprint.domain.model.Status
import com.teachmeprint.domain.model.statusSuccess
import com.teachmeprint.screenshot_domain.model.LanguageTranslationDomain
import com.teachmeprint.screenshot_presentation.R

@Composable
fun ScreenShotTranslateBottomSheet(
    languageTranslationDomain: LanguageTranslationDomain,
    correctedOriginalTextStatus: Status<String>,
    onCorrectedOriginalText: (String) -> Unit,
    isPhraseSaved: Boolean,
    onCheckPhraseInLanguageCollection: (String) -> Unit,
    onSavePhraseInLanguageCollection: (String, String) -> Unit,
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
                    onSavePhraseLanguage = {
                        onSavePhraseInLanguageCollection(
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
                        correctedOriginalText = languageTranslationDomain.originalText,
                        isLoadingStatus = correctedOriginalTextStatus.isLoadingStatus
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
    onSavePhraseLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint = if (isPhraseSaved) { Color.Red } else { MaterialTheme.colorScheme.onSecondaryContainer }

    FilledTonalButton(
        modifier = modifier,
        enabled = !isLoadingStatus,
        onClick = onSavePhraseLanguage
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
        Text(
            modifier = Modifier
                .placeholder(
                    visible = isLoadingStatus,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            text = correctedOriginalText,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun ScreenShotTranslateBottomSheetPreview() {
    ScreenShotTranslateBottomSheet(
        languageTranslationDomain = LanguageTranslationDomain("Original", "Translated"),
        isPhraseSaved = false,
        correctedOriginalTextStatus = statusSuccess("Corrected original"),
        onCorrectedOriginalText = {},
        onCheckPhraseInLanguageCollection = {},
        onSavePhraseInLanguageCollection = { _, _ -> },
        onDismiss = {}
    )
}