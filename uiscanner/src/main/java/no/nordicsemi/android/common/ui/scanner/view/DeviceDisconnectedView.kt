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

package no.nordicsemi.android.common.ui.scanner.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.CircularIcon
import no.nordicsemi.android.common.ui.scanner.R
import no.nordicsemi.android.kotlin.ble.core.data.BleGattConnectionStatus

enum class Reason {
    USER, UNKNOWN, LINK_LOSS, MISSING_SERVICE
}

@Composable
fun DeviceDisconnectedView(
    status: BleGattConnectionStatus,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(PaddingValues) -> Unit = {},
) {
    val disconnectedReason = when (status) {
        BleGattConnectionStatus.UNKNOWN -> stringResource(id = R.string.device_reason_unknown)
        BleGattConnectionStatus.SUCCESS -> stringResource(id = R.string.device_reason_user)
        BleGattConnectionStatus.TERMINATE_LOCAL_HOST -> stringResource(id = R.string.device_reason_terminate_local_host)
        BleGattConnectionStatus.TERMINATE_PEER_USER -> stringResource(id = R.string.device_reason_terminate_peer_user)
        BleGattConnectionStatus.LINK_LOSS -> stringResource(id = R.string.device_reason_link_loss)
        BleGattConnectionStatus.NOT_SUPPORTED -> stringResource(id = R.string.device_reason_missing_service)
        BleGattConnectionStatus.CANCELLED -> stringResource(id = R.string.device_reason_cancelled)
        BleGattConnectionStatus.TIMEOUT -> stringResource(id = R.string.device_reason_timeout)
    }

    DeviceDisconnectedView(disconnectedReason = disconnectedReason, modifier, content)
}

@Composable
fun DeviceDisconnectedView(
    reason: Reason,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(PaddingValues) -> Unit = {},
) {
    val disconnectedReason = when (reason) {
        Reason.USER -> stringResource(id = R.string.device_reason_user)
        Reason.LINK_LOSS -> stringResource(id = R.string.device_reason_link_loss)
        Reason.MISSING_SERVICE -> stringResource(id = R.string.device_reason_missing_service)
        Reason.UNKNOWN -> stringResource(id = R.string.device_reason_unknown)
    }

    DeviceDisconnectedView(disconnectedReason = disconnectedReason, modifier, content)
}

@Composable
fun DeviceDisconnectedView(
    disconnectedReason: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(PaddingValues) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier
                .widthIn(max = 460.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularIcon(imageVector = Icons.Default.HighlightOff)

                Text(
                    text = stringResource(id = R.string.device_disconnected),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = disconnectedReason,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        content(PaddingValues(top = 16.dp))
    }
}

@Preview
@Composable
private fun DeviceDisconnectedView_Preview() {
    NordicTheme {
        DeviceDisconnectedView(
            reason = Reason.MISSING_SERVICE,
            content = { padding ->
                Button(
                    onClick = {},
                    modifier = Modifier.padding(padding)
                ) {
                    Text(text = "Retry")
                }
            }
        )
    }
}
