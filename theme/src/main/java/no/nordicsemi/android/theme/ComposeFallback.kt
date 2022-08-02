/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("unused")

package no.nordicsemi.android.theme

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground
        )
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
        viewEntity.items.onEach {
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
        viewEntity.items.onEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButtonFallback(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
