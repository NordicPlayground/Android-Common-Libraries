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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.R

/**
 * A component that displays list of items grouped by a vertical line on the left.
 *
 * This is to be used in a [WizardStepComponent] to display a list of operations
 * of a Wizard step.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param content The content to be displayed.
 */
@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .then(modifier),
    ) {
        VerticalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 34.dp)
        )
        Column(
            modifier = Modifier.padding(vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            content()
        }
    }
}

/**
 * A vertical divider is a component that displays a vertical line in a Wizard step.
 *
 * Don't use directly, instead wrap the content in a [StatusItem].
 *
 * @param modifier The modifier to be applied to the divider.
 * @param width The width of the divider.
 * @param color The color of the divider.
 * @see StatusItem
 */
@Composable
internal fun VerticalDivider(
    modifier: Modifier = Modifier,
    width: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(width)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
private fun VerticalDividerInWizardPreview() {
    MaterialTheme {
        WizardStepComponent(
            icon = painterResource(R.drawable.baseline_warning_24),
            title = "Identify",
            state = WizardStepState.COMPLETED,
            decor = WizardStepAction.Action(
                text = "Action",
                onClick = { }
            ),
        ) {
            StatusItem {
                Text(text = "Action 1")
                Text(text = "Action 2")
                Text(text = "Action 3")
                Text(text = "Action 4")
            }
        }
    }
}