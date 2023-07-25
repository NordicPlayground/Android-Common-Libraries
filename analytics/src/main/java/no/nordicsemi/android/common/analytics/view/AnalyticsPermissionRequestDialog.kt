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

package no.nordicsemi.android.common.analytics.view

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import no.nordicsemi.android.common.analytics.R
import no.nordicsemi.android.common.analytics.viewmodel.AnalyticsPermissionViewModel
import no.nordicsemi.android.common.core.parseBold

@Composable
fun AnalyticsPermissionRequestDialog() {
    val viewModel: AnalyticsPermissionViewModel = hiltViewModel()
    var show by rememberSaveable { mutableStateOf(false) }

    // The Launch Effect below prevents from blinking the dialog when the app is launched.
    // The wasInfoDialogShown for a brief moment is set to false, even if the user has already
    // seen the dialog. Only when the user setting is loaded, it is set to true.
    LaunchedEffect(viewModel) {
        viewModel.permissionData.collectLatest { permissionData ->
            show = !permissionData.wasInfoDialogShown
        }
    }

    if (show) {
        AnalyticsPermissionRequestDialog(
            onConfirm = { viewModel.onConfirmButtonClick() },
            onDecline = { viewModel.onDeclineButtonClick() }
        )
    }
}

@Composable
fun AnalyticsPermissionRequestDialog(
    onConfirm: () -> Unit,
    onDecline: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDecline,
        title = { Text(stringResource(id = R.string.analytics_dialog_title)) },
        text = {
            Text(
                text = stringResource(id = R.string.analytics_dialog_info).parseBold(),
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.analytics_dialog_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDecline ) {
                Text(stringResource(id = R.string.analytics_dialog_decline))
            }
        },
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(0.95f)
    )
}

@Preview
@Composable
private fun AnalyticsPermissionRequestDialogPreview() {
    AnalyticsPermissionRequestDialog(
        onConfirm = {},
        onDecline = {},
    )
}