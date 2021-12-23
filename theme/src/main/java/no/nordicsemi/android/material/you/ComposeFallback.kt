package no.nordicsemi.android.material.you

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Card(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shape: Shape = RoundedCornerShape(10.dp),
    elevation: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = shape,
        shadowElevation = elevation,
    ) {
        content()
    }
}

@Composable
fun CircularProgressIndicator() {
    Icon(Icons.Default.HourglassTop, "Work in progress.")
}

data class RadioGroupViewEntity(
    val items: List<RadioButtonItem>,
)

data class RadioButtonItem(
    val label: String,
    val isChecked: Boolean = false
)

@Composable
fun RadioButtonGroup(viewEntity: RadioGroupViewEntity, onItemClick: (RadioButtonItem) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        viewEntity.items.onEachIndexed { i, it ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
