package com.lingshot.completephrase_presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder

@Composable
fun CompletePhraseTranslateCard(translateText: String, modifier: Modifier = Modifier) {
    var isTranslatedTextVisible by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .placeholder(
                        visible = !isTranslatedTextVisible,
                        highlight = PlaceholderHighlight.fade()
                    ),
                text = translateText,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = {
                isTranslatedTextVisible = !isTranslatedTextVisible
            }) {
                Icon(
                    imageVector =
                    if (isTranslatedTextVisible) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletePhraseTranslateCardPreview() {
    CompletePhraseTranslateCard("Vamos lá!")
}
