package com.teachmeprint.screenshot_presentation.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teachmeprint.screenshot_presentation.ui.component.NavigationBarItem.TRANSLATE
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ScreenShotNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier.clip(CircleShape)
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        content()
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun RowScope.ScreenShotNavigationBarItem(
    navigationBarItem: NavigationBarItem,
    navigationBarItemList: ImmutableList<NavigationBarItem>,
    modifier: Modifier = Modifier,
    onSelectedOptionsNavigationBar: (NavigationBarItem) -> Unit
) {
    navigationBarItemList.forEach { item ->
        NavigationBarItem(
            modifier = modifier,
            icon = {
                Icon(imageVector = item.icon, contentDescription = item.name)
            },
            label = { Text(item.label) },
            selected = (navigationBarItem == item),
            onClick = {
                onSelectedOptionsNavigationBar(item)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotNavigationBarPreview() {
    ScreenShotNavigationBar {
        ScreenShotNavigationBarItem(
            navigationBarItem = TRANSLATE,
            navigationBarItemList =
            enumValues<NavigationBarItem>()
                .toList()
                .toImmutableList(),
            onSelectedOptionsNavigationBar = {}
        )
    }
}

enum class NavigationBarItem(val icon: ImageVector) {
    TRANSLATE(Icons.Default.Translate),
    LISTEN(Icons.Default.VolumeUp),
    FOCUS(Icons.Default.ZoomOutMap),
    LANGUAGE(Icons.Default.Language);

    val label: String =
        name.lowercase()
            .replaceFirstChar { it.titlecase() }
}