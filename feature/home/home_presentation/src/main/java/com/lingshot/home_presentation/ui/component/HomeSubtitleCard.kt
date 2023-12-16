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
package com.lingshot.home_presentation.ui.component

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.designsystem.component.placeholder.PlaceholderHighlight
import com.lingshot.designsystem.component.placeholder.fade
import com.lingshot.designsystem.component.placeholder.placeholder
import com.lingshot.home_presentation.R
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeSubtitleCard(
    hasPremiumPermission: Boolean?,
    offeringText: String?,
    isEnabled: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClickChanged: () -> Unit,
) {
    val context = LocalContext.current

    OutlinedCard(
        modifier = modifier
            .clickable(onClick = onClickChanged)
            .placeholder(
                visible = (hasPremiumPermission == null),
                highlight = PlaceholderHighlight.fade(),
            ),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.text_title_subtitle_for_manga_home),
                        fontWeight = FontWeight.Bold,
                    )
                },
                trailingContent = {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.icon_assign_premium),
                        contentDescription = null,
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                    )
                },
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.text_label_use_any_source_subtitle_home),
                fontWeight = FontWeight.Bold,
            )
        }
        SubtitleRecommendationCard(context = context)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = stringResource(id = R.string.text_description_subtitle_for_manga_home))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (hasPremiumPermission == false) {
                    Text(
                        text = stringResource(R.string.text_label_free_trial_home),
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ElevatedButton(
                    colors = if (isSelected) {
                        ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    } else {
                        ButtonDefaults.elevatedButtonColors()
                    },
                    enabled = isEnabled,
                    onClick = onClickChanged,
                ) {
                    Text(
                        text =
                        if (hasPremiumPermission == false) {
                            offeringText.toString()
                        } else {
                            stringResource(
                                if (isSelected) {
                                    R.string.text_button_stop_reading_subtitle_home
                                } else {
                                    R.string.text_button_start_reading_subtitle_home
                                },
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SubtitleRecommendationCard(context: Context) {
    val listRecommendationCard = enumValues<RecommendationCard>().toList().toImmutableList()

    LazyRow(
        modifier = Modifier
            .padding(vertical = 16.dp),
        content = {
            itemsIndexed(listRecommendationCard) { position, item ->
                Column(
                    modifier = Modifier
                        .then(
                            if (position == listRecommendationCard.size.minus(1)) {
                                Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                            } else {
                                Modifier.padding(start = 16.dp)
                            },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterVertically,
                    ),
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                            )
                            .clickable { item.openApp(context) },
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = item.logo),
                        contentDescription = null,
                    )
                    Text(text = item.label, fontSize = 12.sp)
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeSubtitleCardPreview() {
    HomeSubtitleCard(
        offeringText = "R$ 2,00",
        hasPremiumPermission = false,
        isEnabled = true,
        isSelected = false,
        onClickChanged = {},
    )
}

enum class RecommendationCard(val label: String, val logo: Int) {
    KINDLE_APP(label = "Kindle App", logo = R.drawable.kindle_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "com.amazon.kindle", isMarketplace)
        }
    },
    KOTATSU(label = "Kotatsu", logo = R.drawable.kotatsu_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "https://kotatsu.app/", false)
        }
    },
    TACHIYOMI(label = "Tachiyomi", logo = R.drawable.tachiyomi_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "https://tachiyomi.org/", false)
        }
    },
    MANGA_PLUS(label = "Manga+", logo = R.drawable.manga_plus_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "jp.co.shueisha.mangaplus", isMarketplace)
        }
    },
    TAPAS(label = "Tapas", logo = R.drawable.tapas_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "com.tapastic", isMarketplace)
        }
    },
    TOOMICS(label = "Toomics", logo = R.drawable.toomics_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "com.toomics.global.google", isMarketplace)
        }
    },
    WEBTOON(label = "Webtoon", logo = R.drawable.webtoon_logo) {
        override fun openApp(context: Context, link: String, isMarketplace: Boolean) {
            super.openApp(context, "com.naver.linewebtoon", isMarketplace)
        }
    },
    ;

    @Suppress("SwallowedException")
    open fun openApp(context: Context, link: String = "", isMarketplace: Boolean = true) {
        if (isMarketplace) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$link"),
                    ),
                )
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$link"),
                    ),
                )
            }
        } else {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(link),
                ),
            )
        }
    }
}
