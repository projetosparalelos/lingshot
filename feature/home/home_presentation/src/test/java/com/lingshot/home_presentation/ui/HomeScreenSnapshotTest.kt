package com.lingshot.home_presentation.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.model.statusSuccess
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.navigation.HomeDestination
import com.phrase.phrasemaster_domain.model.CollectionInfoDomain
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import org.junit.Rule
import org.junit.Test

internal class HomeScreenSnapshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        environment = detectEnvironment().run {
            copy(compileSdkVersion = 33, platformDir = platformDir.replace("34", "33"))
        },
        deviceConfig = DeviceConfig.PIXEL_4A
    )

    @Test
    fun homeScreen_Displayed_By_Default_And_Loading() {
        paparazzi.snapshot {
            LingshotTheme {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = HomeUiState()
                )
            }
        }
    }

    @Test
    fun homeScreen_Displayed_With_Dark_Mode_And_Loading() {
        paparazzi.snapshot {
            LingshotTheme(isDarkTheme = true) {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = HomeUiState()
                )
            }
        }
    }

    @Test
    fun homeScreen_Displayed_By_Default_And_Success() {
        paparazzi.snapshot {
            LingshotTheme {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = fakeUiStateSuccess()
                )
            }
        }
    }

    @Test
    fun homeScreen_Displayed_With_Dark_Mode_And_Success() {
        paparazzi.snapshot {
            LingshotTheme(isDarkTheme = true) {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = fakeUiStateSuccess()
                )
            }
        }
    }

    @Test
    fun homeScreen_Displayed_By_Default_And_Goals_Dialog_Visible() {
        paparazzi.snapshot {
            LingshotTheme {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = fakeUiStateSuccess().copy(isSetGoalsDialogVisible = true)
                )
            }
        }
    }

    @Test
    fun homeScreen_Displayed_With_Dark_Mode_And_Goals_Dialog_Visible() {
        paparazzi.snapshot {
            LingshotTheme(isDarkTheme = true) {
                HomeScreen(
                    homeDestination = HomeDestination(),
                    handleEvent = {},
                    uiState = fakeUiStateSuccess().copy(isSetGoalsDialogVisible = true)
                )
            }
        }
    }

    private fun fakeUiStateSuccess() =
        HomeUiState(
            userDomain = UserDomain(
                username = "UserTest"
            ),
            isPieChartGoalsVisible = true,
            languageCollectionsStatus = statusSuccess(
                Pair(
                    listOf(LanguageCollectionDomain(from = "en", to = "pt")),
                    CollectionInfoDomain(listOf(10), listOf(10))
                )
            ),
            phrasesPendingReviewStatus = statusSuccess("1"),
            consecutiveDaysStatus = statusSuccess(1),
            goals = Pair(
                UserLocalDomain(goal = 10),
                GoalsDomain(progressPhrases = 5)
            )
        )
}
