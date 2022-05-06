package no.nordicsemi.android.material.you

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckboxFallback(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Checkbox(checked, onCheckedChange, modifier, enabled, interactionSource, colors)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioButtonFallback(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: RadioButtonColors = RadioButtonDefaults.colors()
) {
    RadioButton(selected, onClick, modifier, enabled, interactionSource, colors)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
//        colors = DefaultCardColors(
//            containerColor = MaterialTheme.colorScheme.background,
//            contentColor = MaterialTheme.colorScheme.onBackground,
//            disabledContainerColor = MaterialTheme.colorScheme.background,
//            disabledContentColor = MaterialTheme.colorScheme.onBackground
//        )
    ) {
        content()
    }
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
                RadioButtonFallback(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun HorizontalLabelRadioButtonGroup(
    viewEntity: RadioGroupViewEntity,
    onItemClick: (RadioButtonItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        viewEntity.items.onEachIndexed { i, it ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButtonFallback(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
