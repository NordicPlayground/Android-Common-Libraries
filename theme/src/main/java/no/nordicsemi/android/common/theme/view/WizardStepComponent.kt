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

package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

sealed class WizardStepAction {
    /**
     * A button to perform an action.
     * @property text The title.
     * @property onClick The action to perform when the button is clicked.
     * @property enabled Whether the button is enabled.
     * @property dangerous Whether the button is dangerous (red), or not (normal).
     */
    class Action(
        val text: String,
        val onClick: () -> Unit = {},
        val enabled: Boolean = true,
        val dangerous: Boolean = false,
    ): WizardStepAction()

    /**
     * Will display a circular progress indicator.
     */
    object ProgressIndicator: WizardStepAction()
}

enum class WizardStepState {
    /** Future step. */
    INACTIVE,
    /** Current step. */
    CURRENT,
    /** Completed step. */
    COMPLETED,
}

/**
 * A wizard step component.
 *
 * @param icon The icon will be placed in a circular container.
 * @param title The title of the step.
 * @param state Current state of the step.
 * @param decor An optional action or decoration that will be shown on the right.
 */
@Composable
fun WizardStepComponent(
    icon: ImageVector,
    title: String,
    state: WizardStepState,
    modifier: Modifier = Modifier,
    decor: WizardStepAction? = null,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    showVerticalDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularIcon(icon, enabled = state != WizardStepState.INACTIVE)

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(16.dp))

            when (decor) {
                is WizardStepAction.Action -> {
                    ActionButton(action = decor, state = state)
                }
                is WizardStepAction.ProgressIndicator -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    )
                }
                else -> {}
            }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min)
        ) {
            if (showVerticalDivider) {
                VerticalDivider(
                    modifier = Modifier.padding(start = 18.dp, end = 26.dp)
                )
            }

            CompositionLocalProvider(
                LocalContentColor provides if (state == WizardStepState.INACTIVE)
                    contentColor.copy(alpha = 0.38f) else contentColor
            ) {
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    action: WizardStepAction.Action,
    state: WizardStepState,
    modifier: Modifier = Modifier,
) {
    if (state == WizardStepState.COMPLETED && action.enabled) {
        OutlinedButton(
            modifier = modifier,
            onClick = action.onClick,
            enabled = action.enabled,
            colors = if (action.dangerous) {
                ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            }
        ) {
            Text(text = action.text)
        }
    } else {
        Button(
            modifier = modifier,
            onClick = action.onClick,
            enabled = action.enabled,
            colors = if (action.dangerous) {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            } else {
                ButtonDefaults.buttonColors()
            },
        ) {
            Text(text = action.text)
        }
    }
}