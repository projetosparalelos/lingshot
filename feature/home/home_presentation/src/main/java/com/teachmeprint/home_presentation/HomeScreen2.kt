package com.teachmeprint.home_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teachmeprint.designsystem.theme.TeachMePrintTheme

@Composable
fun HomeScreen2() {
    TeachMePrintTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Sua ofensiva é: ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                        append("5 dias")
                    }
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.titleLarge
                )
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    shape = CircleShape
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier)
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.ArrowRight, contentDescription = null
                        )
                        Text(
                            text = "5",
                            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "frases pendentes para ser revisadas.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row(
                    Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val cardModifier = Modifier
                        .weight(1f)
                        .height(150.dp)

                    ElevatedCard(
                        modifier = cardModifier,
                    ) {
                        // Conteúdo da primeira carta
                    }

                    ElevatedCard(
                        modifier = cardModifier,
                    ) {
                        // Conteúdo da segunda carta
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen2Preview() {
    TeachMePrintTheme {
        HomeScreen2()
    }
}