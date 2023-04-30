package com.teachmeprint.language.feature.screenshot.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationBarItem(val icon: ImageVector) {
    TRANSLATE(Icons.Default.Translate),
    LISTEN(Icons.Default.VolumeUp),
    FOCUS(Icons.Default.ZoomOutMap),
    LANGUAGE(Icons.Default.Language);

    val label: String =
        name.lowercase()
            .replaceFirstChar { it.titlecase() }
}