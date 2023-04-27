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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.feature.screenshot.model.ActionCropImageType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType
import com.teachmeprint.language.feature.screenshot.model.NavigationBarItemType.*

@Composable
fun ScreenShotNavigationBar(
    modifier: Modifier = Modifier,
    isNotLoading: Boolean,
    navigationBarItemsType: List<NavigationBarItemType>,
    onCroppedImage: (ActionCropImageType?) -> Unit,
    onToggleTypeIndicatorEnum: (TypeIndicatorEnum) -> Unit,
    onShowDialogLanguage: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(TRANSLATE) }

    NavigationBar(
        modifier = modifier
            .padding(bottom = 16.dp)
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
                    if (isNotLoading) {
                        selectedItem = item
                        when (item) {
                            TRANSLATE -> {
                                onCroppedImage(ActionCropImageType.CROPPED_IMAGE)
                                onToggleTypeIndicatorEnum(TypeIndicatorEnum.TRANSLATE)
                            }
                            LISTEN -> {
                                onCroppedImage(ActionCropImageType.CROPPED_IMAGE)
                                onToggleTypeIndicatorEnum(TypeIndicatorEnum.LISTEN)
                            }
                            FOCUS -> {
                                onCroppedImage(ActionCropImageType.FOCUS_IMAGE)
                            }
                            LANGUAGE -> {
                                onShowDialogLanguage()
                            }
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenShotNavigationBarPreview() {
    ScreenShotNavigationBar(
        navigationBarItemsType = enumValues<NavigationBarItemType>().toList(),
        isNotLoading = true,
        onCroppedImage = {},
        onToggleTypeIndicatorEnum = {},
        onShowDialogLanguage = {}
    )
}