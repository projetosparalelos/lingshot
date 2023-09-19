@file:Suppress("unused", "LongParameterList")

package com.lingshot.testing.helper

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.DeviceConfig.Companion.NEXUS_4
import app.cash.paparazzi.DeviceConfig.Companion.NEXUS_7_2012
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6_PRO
import app.cash.paparazzi.Environment
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import com.lingshot.designsystem.theme.LingshotTheme

val paparazziRealSize = Paparazzi(
    renderingMode = SessionParams.RenderingMode.V_SCROLL,
    showSystemUi = false
)

enum class DefaultTestDevices(val deviceConfig: DeviceConfig) {
    PHONE_SMALL(deviceConfig = NEXUS_4),
    PHONE_NORMAL(deviceConfig = PIXEL_6),
    PHONE_LARGE(deviceConfig = PIXEL_6_PRO),
    TABLET(deviceConfig = NEXUS_7_2012);
}

enum class MultiTheme {
    LIGHT,
    DARK;
}

fun Paparazzi.snapshotMultiDevice(
    defaultTestDevices: DefaultTestDevices,
    multiTheme: MultiTheme,
    name: String? = null,
    theme: String? = null,
    renderingMode: SessionParams.RenderingMode? = null,
    isRealSize: Boolean = false,
    composable: @Composable () -> Unit
) {
    val deviceConfig = if (isRealSize) {
        defaultTestDevices.deviceConfig.copy(softButtons = false, screenHeight = 1)
    } else {
        defaultTestDevices.deviceConfig
    }
    unsafeUpdateConfig(
        deviceConfig = deviceConfig,
        theme = theme,
        renderingMode = renderingMode
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
