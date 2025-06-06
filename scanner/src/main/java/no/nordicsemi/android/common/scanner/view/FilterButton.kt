package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun FilterButton(
    title: String,
    isSelected: Boolean,
    containerColorEnabled: Color = ButtonDefaults.buttonColors().containerColor,
    containerColorDisabled: Color = ButtonDefaults.buttonColors().disabledContainerColor,
    onClick: () -> Unit = {},
) {
    val containerColor = if (isSelected)
        containerColorEnabled else containerColorDisabled
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Close else Icons.Default.Done,
                contentDescription = null,
            )
            Text(title)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    FilterButton(
        title = "Nearby",
        isSelected = true
    )
}
