package no.nordicsemi.android.material.you

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Card(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
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
    val onItemClick: (RadioButtonItem) -> Unit
) {
    fun copyWithNewItem(item: RadioButtonItem): RadioGroupViewEntity {
        if (item.isChecked) return this

        val oldIndex = items.indexOf(item)
        val newItems = items.elementAtOrNull(oldIndex)?.let {
            items.mapIndexed { i, item ->
                when (i) {
                    oldIndex -> item.copy(isChecked = true)
                    else -> item.copy(isChecked = false)
                }
            }
        } ?: throw IllegalArgumentException("Item is not added to the RadioGroup.")
        return copy(items = newItems)
    }
}

data class RadioButtonItem(
    val label: String,
    val isChecked: Boolean = false
)

@Composable
fun RadioButtonGroup(viewEntity: RadioGroupViewEntity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        viewEntity.items.onEach {
            RadioButton(selected = it.isChecked, onClick = { viewEntity.onItemClick(it) })
        }
    }
}
