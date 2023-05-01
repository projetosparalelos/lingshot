package com.teachmeprint.language.presentation.screenshot.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun ScreenShotLanguageDialog(
    modifier: Modifier = Modifier,
    availableLanguage: AvailableLanguage?,
    availableLanguageList: List<AvailableLanguage>,
    onSaveLanguage: (AvailableLanguage?) -> Unit,
    onSelectedOptionsLanguage: (AvailableLanguage?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier.height(500.dp),
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.text_button_dialog_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSaveLanguage(availableLanguage)
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
            ScreenShotLanguageRadioButton(
                availableLanguage = availableLanguage,
                availableLanguageList = availableLanguageList,
                onSelectedOptionsLanguage = onSelectedOptionsLanguage
            )
        }
    )
}

@Composable
private fun ScreenShotLanguageRadioButton(
    availableLanguage: AvailableLanguage?,
    availableLanguageList: List<AvailableLanguage>,
    onSelectedOptionsLanguage: (AvailableLanguage?) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(availableLanguageList, key = { it.languageCode }) {language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == availableLanguage),
                        onClick = {
                            onSelectedOptionsLanguage(language)
                        }
                    ).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (language == availableLanguage),
                    onClick = null
                )
                Text(
                    text = language.displayName,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        availableLanguage?.let {
            val index = availableLanguageList.indexOf(it)
            if (index >= 0) {
                lazyListState.scrollToItem(index)
            }
        }
    }
}

@Preview
@Composable
private fun ScreenShotLanguageDialogPreview() {
    ScreenShotLanguageDialog(
        availableLanguage = AvailableLanguage.ENGLISH,
        availableLanguageList = enumValues<AvailableLanguage>().toList(),
        onSaveLanguage = {},
        onSelectedOptionsLanguage = {},
        onDismiss = {}
    )
}
