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
package com.lingshot.home_domain.model

import com.lingshot.home_domain.R

data class ScreenShotActionDomain(
    val typeActionScreenshot: TypeActionScreenshot,
    val title: Int,
    val description: Int,
)

enum class TypeActionScreenshot {
    FLOATING_BALLOON,
    DEVICE_BUTTON,
}

val screenShotActions = listOf(
    ScreenShotActionDomain(
        typeActionScreenshot = TypeActionScreenshot.FLOATING_BALLOON,
        title = R.string.text_title_by_floating_balloon_home,
        description = R.string.text_description_floating_balloon_home,
    ),
    ScreenShotActionDomain(
        typeActionScreenshot = TypeActionScreenshot.DEVICE_BUTTON,
        title = R.string.text_title_by_device_button_home,
        description = R.string.text_description_device_button_home,
    ),
)
