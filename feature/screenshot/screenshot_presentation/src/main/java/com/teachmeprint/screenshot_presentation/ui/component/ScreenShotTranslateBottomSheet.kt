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
import com.teachmeprint.common.helper.Status
import com.teachmeprint.common.helper.StatusMessage.getErrorMessage
import com.teachmeprint.common.helper.isLoadingStatus
import com.teachmeprint.common.helper.onError
import com.teachmeprint.common.helper.onLoading
import com.teachmeprint.common.helper.onSuccess
import com.teachmeprint.common.helper.statusSuccess
import com.teachmeprint.screenshot_domain.model.LanguageTranslationDomain
import com.teachmeprint.screenshot_presentation.R

@Composable
fun ScreenShotTranslateBottomSheet(
    languageTranslationDomain: LanguageTranslationDomain,
    correctedOriginalTextStatus: Status<String>,
    onCorrectedOriginalText: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

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
                ScreenShotButtonAddToList()
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
                        originalText = languageTranslationDomain.originalText,
                        status = correctedOriginalTextStatus,
                        onCorrectedOriginalText = onCorrectedOriginalText
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ScreenShotButtonAddToList(modifier: Modifier = Modifier) {
    var added by remember { mutableStateOf(false) }
    val iconTint = if (added) { Color.Red } else { MaterialTheme.colorScheme.onSecondaryContainer }

    FilledTonalButton(
        modifier = modifier,
        onClick = { added = !added }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Favorite,
                tint = iconTint,
                contentDescription = null
            )
            AnimatedVisibility(
                visible = added,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(modifier = Modifier.padding(start = 8.dp), text = "Added")
            }
        }
    }
}

@Composable
private fun ScreenShotCorrectedOriginalText(
    originalText: String,
    status: Status<String>,
    modifier: Modifier = Modifier,
    onCorrectedOriginalText: (String) -> Unit
) {
    var correctedOriginalText by remember { mutableStateOf(originalText) }

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
                    visible = status.isLoadingStatus,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            text = correctedOriginalText,
            fontSize = 14.sp
        )
        status
            .onLoading { correctedOriginalText = originalText }
            .onSuccess { correctedOriginalText = it }
            .onError { correctedOriginalText = stringResource(id = getErrorMessage(it)) }
    }
    LaunchedEffect(Unit) {
        onCorrectedOriginalText(originalText)
    }
}

@Preview
@Composable
private fun ScreenShotTranslateBottomSheetPreview() {
    ScreenShotTranslateBottomSheet(
        languageTranslationDomain = LanguageTranslationDomain("Original", "Translated"),
        correctedOriginalTextStatus = statusSuccess("Corrected original"),
        onCorrectedOriginalText = {},
        onDismiss = {}
    )
}