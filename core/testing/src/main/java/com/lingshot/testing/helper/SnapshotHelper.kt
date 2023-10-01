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
@file:Suppress("unused", "LongParameterList")

package com.lingshot.testing.helper

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.DeviceConfig.Companion.NEXUS_4
import app.cash.paparazzi.DeviceConfig.Companion.NEXUS_7_2012
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import app.cash.paparazzi.Environment
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import com.lingshot.designsystem.theme.LingshotTheme

val paparazziRealSize = Paparazzi(
    renderingMode = SessionParams.RenderingMode.V_SCROLL,
    showSystemUi = false,
)

enum class DefaultTestDevices(val deviceConfig: DeviceConfig) {
    PHONE_COMPACT(deviceConfig = NEXUS_4),
    PHONE_STANDARD(deviceConfig = PIXEL_6),
    TABLET_STANDARD(deviceConfig = NEXUS_7_2012),
}

enum class LocaleLanguage(
    val code: String,
) {
    ENGLISH("en"),
    FRENCH("fr"),
    PORTUGUESE("pt"),
    SPANISH("es"),
    SWEDISH("sv"),
}

enum class MultiTheme {
    LIGHT,
    DARK,
}

fun Paparazzi.snapshotMultiDevice(
    defaultTestDevices: DefaultTestDevices,
    multiTheme: MultiTheme,
    localeLanguage: LocaleLanguage,
    name: String? = null,
    theme: String? = null,
    renderingMode: SessionParams.RenderingMode? = null,
    isRealSize: Boolean = false,
    composable: @Composable () -> Unit,
) {
    val deviceConfig = if (isRealSize) {
        defaultTestDevices.deviceConfig.copy(screenHeight = 1)
    } else {
        defaultTestDevices.deviceConfig
    }
    unsafeUpdateConfig(
        deviceConfig = deviceConfig.copy(softButtons = false, locale = localeLanguage.code),
        theme = theme,
        renderingMode = renderingMode,
    )
    snapshot(name = name) {
        LingshotTheme(isDarkTheme = (multiTheme == MultiTheme.DARK)) {
            composable.invoke()
        }
    }
}

fun replaceCompileSdkToSnapshot(): Environment {
    return detectEnvironment().run {
        copy(compileSdkVersion = 33, platformDir = platformDir.replace("34", "33"))
    }
}
