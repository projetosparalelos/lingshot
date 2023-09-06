package com.lingshot.screenshot_presentation.ui.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingshot.screenshot_presentation.R
import com.lingshot.screenshot_presentation.ui.component.NavigationBarItem.TRANSLATE
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
            label = { Text(stringResource(id = item.label)) },
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

enum class NavigationBarItem(val label: Int, val icon: ImageVector) {
    TRANSLATE(
        label = R.string.text_label_navigation_bar_item_translate,
        icon = Icons.Default.Translate
    ),
    LISTEN(label = R.string.text_label_navigation_bar_item_listen, icon = Icons.Default.VolumeUp),
    FOCUS(label = R.string.text_label_navigation_bar_item_focus, icon = Icons.Default.ZoomOutMap),
    I_SPEAK(label = R.string.text_label_navigation_bar_item_i_speak, icon = Icons.Default.Language);
}
