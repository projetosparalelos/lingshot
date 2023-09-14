package com.lingshot.home_presentation.ui.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lingshot.designsystem.theme.LingshotTheme
import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.home_presentation.HomeUiState
import org.junit.Rule
import org.junit.Test

class HomePieChartCardSnapshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_4A,
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    @Test
    fun homePieChart_Displayed_By_Default() {
        paparazzi.snapshot {
            LingshotTheme {
                HomePierChartCard(
                    goals = Pair(
                        UserLocalDomain(),
                        GoalsDomain()
                    ),
                    isSetGoalsDialogVisible = false,
                    selectedGoalDays = 1,
                    listCountPhrases = HomeUiState().goalDaysList,
                    onSelectedGoalDays = {},
                    onSaveGoals = {},
                    onToggleSetGoalsDialog = {}
                )
            }
        }
    }

    @Test
    fun homePieChart_Displayed_With_Dark_Mode() {
        paparazzi.snapshot {
            LingshotTheme(isDarkTheme = true) {
                HomePierChartCard(
                    goals = Pair(
                        UserLocalDomain(),
                        GoalsDomain()
                    ),
                    isSetGoalsDialogVisible = false,
                    selectedGoalDays = 1,
                    listCountPhrases = HomeUiState().goalDaysList,
                    onSelectedGoalDays = {},
                    onSaveGoals = {},
                    onToggleSetGoalsDialog = {}
                )
            }
        }
    }

    @Test
    fun homePieChart_Displayed_By_Default_And_Selected_Goal() {
        paparazzi.snapshot {
            LingshotTheme {
                HomePierChartCard(
                    goals = Pair(
                        UserLocalDomain(goal = 10),
                        GoalsDomain(progressPhrases = 2)
                    ),
                    isSetGoalsDialogVisible = false,
                    selectedGoalDays = 10,
                    listCountPhrases = HomeUiState().goalDaysList,
                    onSelectedGoalDays = {},
                    onSaveGoals = {},
                    onToggleSetGoalsDialog = {}
                )
            }
        }
    }

    @Test
    fun homePieChart_Displayed_With_Dark_Mode_And_Selected_Goal() {
        paparazzi.snapshot {
            LingshotTheme(isDarkTheme = true) {
                HomePierChartCard(
                    goals = Pair(
                        UserLocalDomain(goal = 10),
                        GoalsDomain(progressPhrases = 2)
                    ),
                    isSetGoalsDialogVisible = false,
                    selectedGoalDays = 10,
                    listCountPhrases = HomeUiState().goalDaysList,
                    onSelectedGoalDays = {},
                    onSaveGoals = {},
                    onToggleSetGoalsDialog = {}
                )
            }
        }
    }
}
