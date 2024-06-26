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

package no.nordicsemi.android.common.test.main.page

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.ui.view.PagerViewItem
import no.nordicsemi.android.common.ui.view.ProgressItem
import no.nordicsemi.android.common.ui.view.ProgressItemStatus
import no.nordicsemi.android.common.ui.view.StatusItem
import no.nordicsemi.android.common.ui.view.WizardStepAction
import no.nordicsemi.android.common.ui.view.WizardStepComponent
import no.nordicsemi.android.common.ui.view.WizardStepState

val WizardPage = PagerViewItem("Wizard") {
    WizardScreen()
}

@Composable
private fun WizardScreen() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        OutlinedCard(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                WizardStepComponent(
                    icon = Icons.Default.Warning,
                    title = stringResource(id = R.string.wizard_step_completed),
                    state = WizardStepState.COMPLETED,
                    decor = WizardStepAction.Action(
                        text = stringResource(id = R.string.action_no_op),
                        onClick = { }
                    ),
                ) {
                    StatusItem {
                        Text(text = stringResource(id = R.string.wizard_text_completed))
                    }
                }
                WizardStepComponent(
                    icon = Icons.Default.AccountBox,
                    title = stringResource(id = R.string.wizard_step_current),
                    state = WizardStepState.CURRENT,
                    decor = WizardStepAction.Action(
                        text = stringResource(id = R.string.action_no_op),
                        onClick = { }
                    ),
                ) {
                    StatusItem {
                        Text(text = stringResource(id = R.string.wizard_text_current))
                    }
                }
                WizardStepComponent(
                    icon = Icons.Default.AccountCircle,
                    title = stringResource(id = R.string.wizard_step_in_progress),
                    state = WizardStepState.CURRENT,
                    decor = WizardStepAction.ProgressIndicator,
                ) {
                    ProgressItem(
                        text = stringResource(id = R.string.wizard_text_completed),
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
                        Text(text = stringResource(id = R.string.wizard_task_in_progress))
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

                    ProgressItem(
                        text = stringResource(id = R.string.wizard_task_next),
                        status = ProgressItemStatus.DISABLED,
                    )

                    ProgressItem(
                        text = stringResource(id = R.string.wizard_task_error),
                        status = ProgressItemStatus.ERROR,
                    )
                }
                WizardStepComponent(
                    icon = Icons.Default.Build,
                    title = stringResource(id = R.string.wizard_step_inactive),
                    state = WizardStepState.INACTIVE,
                    decor = WizardStepAction.Action(
                        text = stringResource(id = R.string.action_no_op),
                        dangerous = true,
                        onClick = { }
                    ),
                ) {
                    StatusItem {
                        Text(text = stringResource(id = R.string.wizard_text_inactive))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    NordicTheme {
        WizardScreen()
    }
}