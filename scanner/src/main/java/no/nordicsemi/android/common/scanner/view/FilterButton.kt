package no.nordicsemi.android.common.scanner.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun FilterButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    containerColorEnabled: Color = Color.Unspecified,
    containerColorDisabled: Color = Color.Unspecified,
    onClick: () -> Unit = {},
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(title) },
        leadingIcon = {
            Icon(
                imageVector = if (isSelected) Icons.Default.Close else icon,
                contentDescription = null
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColorDisabled,
            selectedContainerColor = containerColorEnabled,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreviewSelected() {
    FilterButton(
        title = "Nearby",
        icon = Icons.Default.MyLocation,
        isSelected = true
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    FilterButton(
        title = "Named",
        icon = Icons.AutoMirrored.Default.Label,
        isSelected = false
    )
}
