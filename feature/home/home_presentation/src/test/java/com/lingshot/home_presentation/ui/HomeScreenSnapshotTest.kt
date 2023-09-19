package com.lingshot.home_presentation.ui

import app.cash.paparazzi.Paparazzi
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.domain.model.statusSuccess
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.navigation.HomeDestination
import com.lingshot.testing.helper.DefaultTestDevices
import com.lingshot.testing.helper.MultiTheme
import com.lingshot.testing.helper.replaceCompileSdkToSnapshot
import com.lingshot.testing.helper.snapshotMultiDevice
import com.phrase.phrasemaster_domain.model.CollectionInfoDomain
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
internal class HomeScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        environment = replaceCompileSdkToSnapshot()
    )

    @Test
    fun homeScreen_Displayed_By_Default_And_Loading(
        @TestParameter defaultTestDevices: DefaultTestDevices,
        @TestParameter multiTheme: MultiTheme
    ) {
        paparazzi.snapshotMultiDevice(
            defaultTestDevices = defaultTestDevices,
            multiTheme = multiTheme
        ) {
            HomeScreen(
                homeDestination = HomeDestination(),
                handleEvent = {},
                uiState = HomeUiState()
            )
        }
    }

    @Test
    fun homeScreen_Displayed_By_Default_And_Success(
        @TestParameter defaultTestDevices: DefaultTestDevices,
        @TestParameter multiTheme: MultiTheme
    ) {
        paparazzi.snapshotMultiDevice(
            defaultTestDevices = defaultTestDevices,
            multiTheme = multiTheme
        ) {
            HomeScreen(
                homeDestination = HomeDestination(),
                handleEvent = {},
                uiState = fakeUiStateSuccess()
            )
        }
    }

    @Test
    fun homeScreen_Displayed_By_Default_And_Goals_Dialog_Visible(
        @TestParameter defaultTestDevices: DefaultTestDevices,
        @TestParameter multiTheme: MultiTheme
    ) {
        paparazzi.snapshotMultiDevice(
            defaultTestDevices = defaultTestDevices,
            multiTheme = multiTheme
        ) {
            HomeScreen(
                homeDestination = HomeDestination(),
                handleEvent = {},
                uiState = fakeUiStateSuccess().copy(isSetGoalsDialogVisible = true)
            )
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
