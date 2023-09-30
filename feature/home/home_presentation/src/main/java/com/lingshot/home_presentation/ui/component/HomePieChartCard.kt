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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.lingshot.home_presentation.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lingshot.designsystem.theme.LocalSchemeCustom
import com.lingshot.domain.model.GoalsDomain
import com.lingshot.domain.model.UserLocalDomain
import com.lingshot.home_presentation.HomeUiState
import com.lingshot.home_presentation.R
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HomePierChartCard(
    goals: Pair<UserLocalDomain?, GoalsDomain?>?,
    isSetGoalsDialogVisible: Boolean,
    selectedGoalDays: Int,
    listCountPhrases: ImmutableList<Int>,
    onSelectedGoalDays: (Int) -> Unit,
    onSaveGoals: (Int) -> Unit,
    onToggleSetGoalsDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val newGoals = goals?.first?.goal ?: 0
    val completed = goals?.second?.progressPhrases ?: 0

    ElevatedCard {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HomePieChart(
                modifier = Modifier.size(120.dp),
                goals = newGoals,
                completed = completed,
            )
            Column {
                HomePieChartIndicator(
                    value = newGoals.toString(),
                    type = stringResource(R.string.text_label_piechart_goals_home),
                    color = LocalSchemeCustom.current.goalsPieChart,
                )
                HomePieChartIndicator(
                    value = completed.toString(),
                    type = stringResource(R.string.text_label_piechart_completed_home),
                    color = LocalSchemeCustom.current.completedPieChart,
                )
            }
            IconButton(
                onClick = onToggleSetGoalsDialog,
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    }

    if (isSetGoalsDialogVisible) {
        AlertDialog(
            onDismissRequest = onToggleSetGoalsDialog,
            dismissButton = {
                TextButton(onClick = onToggleSetGoalsDialog) {
                    Text(text = stringResource(R.string.text_button_cancel_goals_dialog_home))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onSaveGoals(selectedGoalDays)
                }) {
                    Text(text = stringResource(R.string.text_button_save_goal_dialog_home))
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.text_title_goals_dialog_home),
                    fontSize = 18.sp,
                )
            },
            text = {
                GoalsDropdownMenu(
                    selectedGoalDays = selectedGoalDays,
                    listCountPhrases = listCountPhrases,
                    onSelectedGoalDays = onSelectedGoalDays,
                )
            },
        )
    }
}

@Composable
private fun GoalsDropdownMenu(
    selectedGoalDays: Int,
    listCountPhrases: ImmutableList<Int>,
    onSelectedGoalDays: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .focusProperties {
                    canFocus = false
                },
            value = stringResource(
                R.string.text_label_dropdown_menu_item_goals_home,
                selectedGoalDays,
            ),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.text_label_count_dropdown_menu_goals_home),
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listCountPhrases.forEach { selectedOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                R.string.text_label_dropdown_menu_item_goals_home,
                                selectedOption,
                            ),
                        )
                    },
                    onClick = {
                        onSelectedGoalDays(selectedOption)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun HomePieChart(
    goals: Int,
    completed: Int,
    modifier: Modifier = Modifier,
) {
    val schemeCustom = LocalSchemeCustom.current
    Canvas(modifier = modifier) {
        val adjustedCompleted = if (completed > goals) goals else completed
        val chartData = if (goals == 0) {
            listOf(0.5f, 0.5f)
        } else {
            listOf(
                adjustedCompleted.toFloat() / goals,
                (goals - adjustedCompleted).toFloat() / goals,
            )
        }

        val chartDegrees = 360
        val canvasSize = size
        val radius = canvasSize.width / 2
        var startAngle = 0f

        chartData.indices.forEach { index ->
            val sweepAngle = chartDegrees * chartData[index]

            val color = when (index) {
                0 -> schemeCustom.completedPieChart
                1 -> schemeCustom.goalsPieChart
                else -> Color.White
            }

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                size = Size(radius * 2, radius * 2),
                useCenter = true,
                style = Fill,
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
private fun HomePieChartIndicator(value: String, type: String, color: Color) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("$value ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
            append(type)
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePieChartCardPreview() {
    HomePierChartCard(
        goals = Pair(UserLocalDomain(), GoalsDomain()),
        selectedGoalDays = 1,
        isSetGoalsDialogVisible = false,
        listCountPhrases = HomeUiState().goalDaysList,
        onSelectedGoalDays = {},
        onSaveGoals = {},
        onToggleSetGoalsDialog = {},
    )
}
