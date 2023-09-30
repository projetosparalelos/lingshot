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

package com.lingshot.swipepermission_presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.common.util.singleClick
import com.lingshot.swipepermission_presentation.R

@Composable
internal fun SwipePermissionGoogleAuthButton(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = singleClick(onSignIn),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_google),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.text_button_sign_with_google_swiper_permission))
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipePermissionGoogleAuthButtonPreview() {
    SwipePermissionGoogleAuthButton {}
}
