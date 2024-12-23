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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A wizard step action to be displayed on the right side of a step.
 */
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
    data object ProgressIndicator: WizardStepAction()
}

/**
 * The state of a wizard step.
 *
 * A step of a wizard can be in one of the following states:
 * - [INACTIVE] - future step,
 * - [CURRENT] - current step,
 * - [COMPLETED] - completed step.
 */
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
 * Wizard is a common pattern for guiding users through a series of linear steps.
 * With this component you can create a step with an icon, title, optional action and a content.
 *
 * ### Example
 * ```kotlin
 * Box(
 *     modifier = Modifier.fillMaxSize(),
 *     contentAlignment = Alignment.TopCenter
 * ) {
 *     OutlinedCard(
 *         modifier = Modifier
 *             .verticalScroll(rememberScrollState())
 *             .widthIn(max = 600.dp)
 *             .padding(all = 16.dp),
 *     ) {
 *         Column(
 *             modifier = Modifier
 *                 .fillMaxWidth()
 *                 .padding(16.dp),
 *         ) {
 *             WizardStepComponent(
 *                 icon = Icons.Default.Warning,
 *                 title = "Identify",
 *                 state = WizardStepState.COMPLETED,
 *                 decor = WizardStepAction.Action(
 *                     text = "Action",
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Text(text = "Identified")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.AccountBox,
 *                 title = "Very Long Title That Won't Fit",
 *                 state = WizardStepState.CURRENT,
 *                 decor = WizardStepAction.Action(
 *                     text = "Action",
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Text(text = "Select color")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.AccountCircle,
 *                 title = "Connect",
 *                 state = WizardStepState.CURRENT,
 *                 decor = WizardStepAction.ProgressIndicator,
 *             ) {
 *                 ProgressItem(
 *                     text = "Completed",
 *                     status = ProgressItemStatus.SUCCESS,
 *                 )
 *
 *                 val infiniteTransition =
 *                     rememberInfiniteTransition(label = "ProgressTransition")
 *                 val progress by infiniteTransition.animateFloat(
 *                     initialValue = 0.0f,
 *                     targetValue = 1.0f,
 *                     animationSpec = infiniteRepeatable(
 *                         animation = tween(10000, easing = LinearEasing),
 *                         repeatMode = RepeatMode.Restart
 *                     ),
 *                     label = "Progress"
 *                 )
 *
 *                 ProgressItem(
 *                     status = ProgressItemStatus.WORKING,
 *                 ) {
 *                     Column {
 *                         Text(text = "In progress")
 *                         LinearProgressIndicator(
 *                             progress = { progress },
 *                             modifier = Modifier.fillMaxWidth(),
 *                             trackColor = MaterialTheme.colorScheme.surfaceVariant,
 *                             drawStopIndicator = {}
 *                         )
 *                         Text(
 *                             text = "%.1f%%".format(progress * 100),
 *                             modifier = Modifier.fillMaxWidth(),
 *                             textAlign = TextAlign.End
 *                         )
 *                     }
 *                 }
 *
 *                 ProgressItem(
 *                     text = "Future",
 *                     status = ProgressItemStatus.DISABLED,
 *                 )
 *
 *                 ProgressItem(
 *                     text = "Error happened",
 *                     status = ProgressItemStatus.ERROR,
 *                 )
 *
 *                 StatusItem {
 *                     Text(text = "Connect to the device")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.Build,
 *                 title = "Destroy",
 *                 state = WizardStepState.INACTIVE,
 *                 decor = WizardStepAction.Action(
 *                     text = "Terminate",
 *                     dangerous = true,
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Row(
 *                         verticalAlignment = Alignment.CenterVertically,
 *                     ) {
 *                         Text(
 *                             text = "Engage warp 4",
 *                             modifier = Modifier.weight(1f)
 *                         )
 *                         var checked by rememberSaveable { mutableStateOf(false) }
 *                         Switch(
 *                             checked = checked,
 *                             onCheckedChange = { checked = it },
 *                         )
 *                     }
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
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
    contentColor: Color = LocalContentColor.current,
    contentStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    content: @Composable ColumnScope.() -> Unit
) {
    WizardStepComponent(
        icon = icon,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        state = state,
        modifier = modifier,
        decor = decor,
        contentColor = contentColor,
        contentStyle = contentStyle,
        content = content,
    )
}

/**
 * A wizard step component.
 *
 * Wizard is a common pattern for guiding users through a series of linear steps.
 * With this component you can create a step with an icon, title, optional action and a content.
 *
 * ### Example
 * ```kotlin
 * Box(
 *     modifier = Modifier.fillMaxSize(),
 *     contentAlignment = Alignment.TopCenter
 * ) {
 *     OutlinedCard(
 *         modifier = Modifier
 *             .verticalScroll(rememberScrollState())
 *             .widthIn(max = 600.dp)
 *             .padding(all = 16.dp),
 *     ) {
 *         Column(
 *             modifier = Modifier
 *                 .fillMaxWidth()
 *                 .padding(16.dp),
 *         ) {
 *             WizardStepComponent(
 *                 icon = Icons.Default.Warning,
 *                 title = "Identify",
 *                 state = WizardStepState.COMPLETED,
 *                 decor = WizardStepAction.Action(
 *                     text = "Action",
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Text(text = "Identified")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.AccountBox,
 *                 title = "Very Long Title That Won't Fit",
 *                 state = WizardStepState.CURRENT,
 *                 decor = WizardStepAction.Action(
 *                     text = "Action",
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Text(text = "Select color")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.AccountCircle,
 *                 title = "Connect",
 *                 state = WizardStepState.CURRENT,
 *                 decor = WizardStepAction.ProgressIndicator,
 *             ) {
 *                 ProgressItem(
 *                     text = "Completed",
 *                     status = ProgressItemStatus.SUCCESS,
 *                 )
 *
 *                 val infiniteTransition =
 *                     rememberInfiniteTransition(label = "ProgressTransition")
 *                 val progress by infiniteTransition.animateFloat(
 *                     initialValue = 0.0f,
 *                     targetValue = 1.0f,
 *                     animationSpec = infiniteRepeatable(
 *                         animation = tween(10000, easing = LinearEasing),
 *                         repeatMode = RepeatMode.Restart
 *                     ),
 *                     label = "Progress"
 *                 )
 *
 *                 ProgressItem(
 *                     status = ProgressItemStatus.WORKING,
 *                 ) {
 *                     Column {
 *                         Text(text = "In progress")
 *                         LinearProgressIndicator(
 *                             progress = { progress },
 *                             modifier = Modifier.fillMaxWidth(),
 *                             trackColor = MaterialTheme.colorScheme.surfaceVariant,
 *                             drawStopIndicator = {}
 *                         )
 *                         Text(
 *                             text = "%.1f%%".format(progress * 100),
 *                             modifier = Modifier.fillMaxWidth(),
 *                             textAlign = TextAlign.End
 *                         )
 *                     }
 *                 }
 *
 *                 ProgressItem(
 *                     text = "Future",
 *                     status = ProgressItemStatus.DISABLED,
 *                 )
 *
 *                 ProgressItem(
 *                     text = "Error happened",
 *                     status = ProgressItemStatus.ERROR,
 *                 )
 *
 *                 StatusItem {
 *                     Text(text = "Connect to the device")
 *                 }
 *             }
 *             WizardStepComponent(
 *                 icon = Icons.Default.Build,
 *                 title = "Destroy",
 *                 state = WizardStepState.INACTIVE,
 *                 decor = WizardStepAction.Action(
 *                     text = "Terminate",
 *                     dangerous = true,
 *                     onClick = { }
 *                 ),
 *             ) {
 *                 StatusItem {
 *                     Row(
 *                         verticalAlignment = Alignment.CenterVertically,
 *                     ) {
 *                         Text(
 *                             text = "Engage warp 4",
 *                             modifier = Modifier.weight(1f)
 *                         )
 *                         var checked by rememberSaveable { mutableStateOf(false) }
 *                         Switch(
 *                             checked = checked,
 *                             onCheckedChange = { checked = it },
 *                         )
 *                     }
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param icon The icon will be placed in a circular container.
 * @param title The title of the step.
 * @param state Current state of the step.
 * @param decor An optional action or decoration that will be shown on the right.
 */
@Composable
fun WizardStepComponent(
    icon: ImageVector,
    title: @Composable () -> Unit,
    state: WizardStepState,
    modifier: Modifier = Modifier,
    decor: WizardStepAction? = null,
    contentColor: Color = LocalContentColor.current,
    contentStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularIcon(icon, enabled = state != WizardStepState.INACTIVE)

            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier.weight(1.0f)) {
                title()
            }

            Spacer(modifier = Modifier.size(16.dp))

            when (decor) {
                is WizardStepAction.Action -> {
                    ActionButton(action = decor, state = state)
                }
                is WizardStepAction.ProgressIndicator -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
                else -> {}
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides if (state == WizardStepState.INACTIVE)
                contentColor.copy(alpha = 0.38f) else contentColor
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ProvideTextStyle(value = contentStyle) {
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

@Preview(showBackground = true)
@Composable
private fun WizardScreen() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            OutlinedCard(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .widthIn(max = 600.dp)
                    .padding(all = 16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    WizardStepComponent(
                        icon = Icons.Default.Warning,
                        title = "Identify",
                        state = WizardStepState.COMPLETED,
                        decor = WizardStepAction.Action(
                            text = "Action",
                            onClick = { }
                        ),
                    ) {
                        StatusItem {
                            Text(text = "Identified")
                        }
                    }
                    WizardStepComponent(
                        icon = Icons.Default.AccountBox,
                        title = "Very Long Title That Won't Fit",
                        state = WizardStepState.CURRENT,
                        decor = WizardStepAction.Action(
                            text = "Action",
                            onClick = { }
                        ),
                    ) {
                        StatusItem {
                            Text(text = "Select color")
                        }
                    }
                    WizardStepComponent(
                        icon = Icons.Default.AccountCircle,
                        title = "Connect",
                        state = WizardStepState.CURRENT,
                        decor = WizardStepAction.ProgressIndicator,
                    ) {
                        ProgressItem(
                            text = "Completed",
                            status = ProgressItemStatus.SUCCESS,
                        )

                        val infiniteTransition =
                            rememberInfiniteTransition(label = "ProgressTransition")
                        val progress by infiniteTransition.animateFloat(
                            initialValue = 0.0f,
                            targetValue = 1.0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(10000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "Progress"
                        )

                        ProgressItem(
                            status = ProgressItemStatus.WORKING,
                        ) {
                            Column {
                                Text(text = "In progress")
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier.fillMaxWidth(),
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    drawStopIndicator = {}
                                )
                                Text(
                                    text = "%.1f%%".format(progress * 100),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                        ProgressItem(
                            text = "Future",
                            status = ProgressItemStatus.DISABLED,
                        )

                        ProgressItem(
                            text = "Error happened",
                            status = ProgressItemStatus.ERROR,
                        )

                        StatusItem {
                            Text(text = "Connect to the device")
                        }
                    }
                    WizardStepComponent(
                        icon = Icons.Default.Build,
                        title = "Destroy",
                        state = WizardStepState.INACTIVE,
                        decor = WizardStepAction.Action(
                            text = "Terminate",
                            dangerous = true,
                            onClick = { }
                        ),
                    ) {
                        StatusItem {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Engage warp 4",
                                    modifier = Modifier.weight(1f)
                                )
                                var checked by rememberSaveable { mutableStateOf(false) }
                                Switch(
                                    checked = checked,
                                    onCheckedChange = { checked = it },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}