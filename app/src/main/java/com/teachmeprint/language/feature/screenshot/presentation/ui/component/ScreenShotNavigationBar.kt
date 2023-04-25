package com.teachmeprint.language.feature.screenshot.presentation.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType.TRANSLATE

@Composable
fun ScreenShotNavigationBar(
    modifier: Modifier = Modifier,
    navigationBarItemsType: List<NavigationBarItemType>,
    onCroppedImage: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(TRANSLATE) }

    NavigationBar(
        modifier = modifier
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .clip(CircleShape)
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        navigationBarItemsType.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.name)
                },
                label = { Text(item.label) },
                selected = selectedItem == item,
                onClick = {
                    selectedItem = item
                    if (item == TRANSLATE) {
                        onCroppedImage()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}