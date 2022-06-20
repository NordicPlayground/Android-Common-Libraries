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

package no.nordicsemi.analytics.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.analytics.R
import no.nordicsemi.analytics.repository.AnalyticsPermissionData
import no.nordicsemi.analytics.viewmodel.AnalyticsPermissionViewModel
import no.nordicsemi.android.theme.CheckboxFallback
import no.nordicsemi.android.theme.parseBold

@Composable
fun AnalyticsPermissionButton() {

    val showDialog = rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { showDialog.value = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_firebase),
            contentDescription = stringResource(id = R.string.analytics_permission_button),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
    AnalyticsPermissionSwitchDialog(showDialog)
}

@Composable
fun AnalyticsPermissionSwitchDialog(showDialog: MutableState<Boolean>) {

    val viewModel: AnalyticsPermissionViewModel = hiltViewModel()
    val data = viewModel.permissionData.collectAsState(AnalyticsPermissionData()).value

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.onDeclineButtonClick() },
            title = { Text(stringResource(id = R.string.analytics_switch_dialog_title)) },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.analytics_switch_dialog_permission),
                            style = MaterialTheme.typography.titleMedium
                        )
                        CheckboxFallback(
                            checked = data.isPermissionGranted,
                            onCheckedChange = { viewModel.onPermissionChanged() }
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.analytics_dialog_info).parseBold(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(id = R.string.analytics_switch_dialog_button))
                }
            },
            modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(0.95f)
        )
    }
}
