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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.home_presentation.R

@Composable
internal fun HomeDonateButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .padding(start = 16.dp)
            .clip(CircleShape)
            .clickable {
                uriHandler.openUri("https://ko-fi.com/lingshot")
            }
            .background(Color(0xFFFF5F5F))
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ko_fi_logo),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(R.string.text_button_donate),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeDonateButtonPreview() {
    HomeDonateButton()
}
