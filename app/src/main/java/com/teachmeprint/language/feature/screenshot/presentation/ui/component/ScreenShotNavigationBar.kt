package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItem
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItem.*

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
    navigationBarItemList: List<NavigationBarItem>,
    onSelectedOptionsNavigationBar: (NavigationBarItem) -> Unit
) {
    navigationBarItemList.forEach { item ->
        NavigationBarItem(
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
            navigationBarItemList = enumValues<NavigationBarItem>().toList(),
            onSelectedOptionsNavigationBar = {}
        )
    }
}
