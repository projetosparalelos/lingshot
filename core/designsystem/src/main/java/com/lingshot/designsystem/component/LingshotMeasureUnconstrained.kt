package com.lingshot.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
fun LingshotMeasureUnconstrained(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp, measuredHeight: Dp) -> Unit
) {
    SubcomposeLayout { constraints ->
        val placeable = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints())

        val measuredWidth = placeable.width.toDp()
        val measuredHeight = placeable.height.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth, measuredHeight)
        }[0].measure(constraints)

        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}
