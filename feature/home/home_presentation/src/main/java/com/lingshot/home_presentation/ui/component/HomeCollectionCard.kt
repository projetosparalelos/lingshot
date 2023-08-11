package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.languagechoice_domain.model.AvailableLanguage
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain

@Composable
fun HomeCollectionCard(
    languageCollectionDomain: LanguageCollectionDomain,
    modifier: Modifier = Modifier,
    onNavigateToCompletePhrase: (String) -> Unit
) {
    val languageFrom = AvailableLanguage.from(languageCollectionDomain.from)
    val languageTo = AvailableLanguage.from(languageCollectionDomain.to)

    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToCompletePhrase(languageCollectionDomain.id)
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                    append("${languageFrom?.flagEmoji} ${languageFrom?.displayName} ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                    append("from ${languageTo?.displayName}")
                }
            }
            Text(text = text)
            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = "Playing 0 / 12.605 sentences",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeCollectionCardPreview() {
    HomeCollectionCard(languageCollectionDomain = LanguageCollectionDomain()) {}
}
