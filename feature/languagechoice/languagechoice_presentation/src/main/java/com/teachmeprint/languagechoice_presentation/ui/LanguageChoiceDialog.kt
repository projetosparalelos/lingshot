package com.teachmeprint.languagechoice_presentation.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.teachmeprint.languagechoice_domain.model.AvailableLanguage
import com.teachmeprint.languagechoice_presentation.R

@Composable
fun LanguageChoiceDialog(
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
            LanguageChoiceList(
                availableLanguage = availableLanguage,
                availableLanguageList = availableLanguageList,
                onSelectedOptionsLanguage = onSelectedOptionsLanguage
            )
        }
    )
}

@Composable
private fun LanguageChoiceList(
    availableLanguage: AvailableLanguage?,
    availableLanguageList: List<AvailableLanguage>,
    onSelectedOptionsLanguage: (AvailableLanguage?) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(availableLanguageList, key = { it.languageCode }) { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == availableLanguage),
                        onClick = {
                            onSelectedOptionsLanguage(language)
                        }
                    )
                    .background(
                        if (language == availableLanguage) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        }
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageChoiceImageFlag(url = language.flag)
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

@Composable
private fun LanguageChoiceImageFlag(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    url: String
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(url)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .placeholder(R.drawable.ic_image_placeholder)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .width(54.dp)
            .height(36.dp)
            .placeholder(
                visible = painter.state is AsyncImagePainter.State.Loading,
                highlight = PlaceholderHighlight.shimmer()
            )
    )
}

@Preview
@Composable
private fun LanguageChoiceDialogPreview() {
    LanguageChoiceDialog(
        availableLanguage = AvailableLanguage.ENGLISH,
        availableLanguageList = enumValues<AvailableLanguage>().toList(),
        onSaveLanguage = {},
        onSelectedOptionsLanguage = {},
        onDismiss = {}
    )
}
