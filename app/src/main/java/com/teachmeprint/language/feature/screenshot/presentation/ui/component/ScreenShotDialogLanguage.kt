package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teachmeprint.language.R
import com.teachmeprint.language.data.model.language.AvailableLanguage

@Composable
fun ScreenShotDialogLanguage(
    modifier: Modifier = Modifier,
    availableLanguages: List<AvailableLanguage>,
    selectedOptionLanguage: AvailableLanguage?,
    onOptionSelectedLanguage: (AvailableLanguage?) -> Unit,
    onSaveLanguage: (AvailableLanguage?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier.height(500.dp),
        onDismissRequest = {
            onDismiss()
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.text_button_dialog_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSaveLanguage(selectedOptionLanguage)
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.text_button_select_dialog_choose_language))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.text_title_dialog_choose_language),
                fontSize = 18.sp
            )
        },
        text = {
            ScreenShotRadioButtonLanguage(
                availableLanguages = availableLanguages,
                selectedOptionLanguage = selectedOptionLanguage,
                onOptionSelectedLanguage = onOptionSelectedLanguage
            )
        }
    )
}

@Composable
private fun ScreenShotRadioButtonLanguage(
    availableLanguages: List<AvailableLanguage>,
    selectedOptionLanguage: AvailableLanguage?,
    onOptionSelectedLanguage: (AvailableLanguage?) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(availableLanguages, key = { it.languageCode }) {language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == selectedOptionLanguage),
                        onClick = {
                            onOptionSelectedLanguage(language)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (language == selectedOptionLanguage),
                    onClick = { onOptionSelectedLanguage(language) }
                )
                Text(
                    text = language.displayName,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        selectedOptionLanguage?.let {
            val index = availableLanguages.indexOf(it)
            if (index >= 0) {
                lazyListState.scrollToItem(index)
            }
        }
    }
}

@Preview
@Composable
fun ScreenShotDialogLanguagePreview() {
}
