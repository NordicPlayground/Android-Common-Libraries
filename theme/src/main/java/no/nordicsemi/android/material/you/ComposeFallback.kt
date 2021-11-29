package no.nordicsemi.android.material.you

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Box(modifier = Modifier.clickable { onCheckedChange() }) {
        if (checked) {
            Icon(Icons.Default.CheckBox, "Checked icon.")
        } else {
            Icon(Icons.Default.CheckBoxOutlineBlank, "Unchecked icon.")
        }
    }
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
            RadioButton(viewEntity = it) {
                viewEntity.onItemClick(it)
            }
        }
    }
}

@Composable
fun RadioButton(viewEntity: RadioButtonItem, onItemClick: (RadioButtonItem) -> Unit) {
    Column(
        modifier = Modifier.clickable { onItemClick(viewEntity) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewEntity.isChecked) {
            Icon(Icons.Default.RadioButtonChecked, "Checked radio button.")
        } else {
            Icon(Icons.Default.RadioButtonUnchecked, "Unchecked radio button.")
        }
        Text(text = viewEntity.label, style = MaterialTheme.typography.labelMedium)
    }
}
