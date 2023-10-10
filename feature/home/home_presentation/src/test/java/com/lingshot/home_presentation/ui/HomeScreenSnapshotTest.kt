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
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lingshot.home_presentation.ui

import app.cash.paparazzi.Paparazzi
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.lingshot.home_presentation.navigation.HomeDestination
import com.lingshot.testing.helper.DefaultTestDevices
import com.lingshot.testing.helper.LocaleLanguage
import com.lingshot.testing.helper.MultiTheme
import com.lingshot.testing.helper.replaceCompileSdkToSnapshot
import com.lingshot.testing.helper.snapshotMultiDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
internal class HomeScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        environment = replaceCompileSdkToSnapshot(),
    )

    @Test
    fun homeScreen_Displayed_By_Default(
        @TestParameter defaultTestDevices: DefaultTestDevices,
        @TestParameter localeLanguage: LocaleLanguage,
        @TestParameter multiTheme: MultiTheme,
    ) {
        paparazzi.snapshotMultiDevice(
            defaultTestDevices = defaultTestDevices,
            localeLanguage = localeLanguage,
            multiTheme = multiTheme,
        ) {
            HomeScreen(
                homeDestination = HomeDestination(),
            )
        }
    }
}
