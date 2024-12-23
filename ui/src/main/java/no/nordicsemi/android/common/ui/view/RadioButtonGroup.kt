/*
 * Copyright (c) 2023, Nordic Semiconductor
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

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Representation of a set of items of radio button group.
 *
 * @param items The radio button group items.
 */
data class RadioGroupViewEntity(
    val items: List<RadioButtonItem>,
)

/**
 * Representation of a radio button item.
 *
 * @param label The label of the radio button.
 * @param isChecked The state of the radio button.
 */
data class RadioButtonItem(
    val label: String,
    val isChecked: Boolean = false
)

/**
 * A radio button group is a component that displays a set of radio buttons
 * in a vertical layout.
 *
 * @param viewEntity The radio button group view entity.
 * @param onItemClick The callback to be invoked when a radio button is clicked.
 */
@Composable
fun RadioButtonGroup(
    viewEntity: RadioGroupViewEntity,
    onItemClick: (RadioButtonItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        viewEntity.items.onEach {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

/**
 * A radio button group is a component that displays a set of radio buttons
 * in a horizontal layout.
 *
 * @param viewEntity The radio button group view entity.
 * @param onItemClick The callback to be invoked when a radio button is clicked.
 */
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
                RadioButton(selected = it.isChecked, onClick = { onItemClick(it) })
                Text(text = it.label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonGroupPreview() {
    MaterialTheme {
        RadioButtonGroup(
            viewEntity = RadioGroupViewEntity(
                listOf(
                    RadioButtonItem(label = "Option 1", isChecked = true),
                    RadioButtonItem(label = "Option 2", isChecked = false),
                    RadioButtonItem(label = "Option 3", isChecked = false),
                    RadioButtonItem(label = "Option 4", isChecked = false),
                )
            ),
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalLabelRadioButtonGroupPreview() {
    MaterialTheme {
        HorizontalLabelRadioButtonGroup(
            viewEntity = RadioGroupViewEntity(
                listOf(
                    RadioButtonItem(label = "Option 1", isChecked = true),
                    RadioButtonItem(label = "Option 2", isChecked = false),
                    RadioButtonItem(label = "Option 3", isChecked = false),
                    RadioButtonItem(label = "Option 4", isChecked = false),
                )
            ),
            onItemClick = {}
        )
    }
}