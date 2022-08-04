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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.android.common.ui.scanner.R
import no.nordicsemi.android.common.ui.scanner.repository.ScanningState
import no.nordicsemi.android.common.ui.scanner.ui.exhaustive

@Composable
internal fun DevicesListView(
    requireLocation: Boolean,
    state: ScanningState,
    row: @Composable (DiscoveredBluetoothDevice) -> Unit,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        item { Spacer(modifier = Modifier.size(8.dp)) }

        when (state) {
            is ScanningState.Loading -> item { ScanEmptyView(requireLocation) }
            is ScanningState.DevicesDiscovered -> {
                if (state.isEmpty()) {
                    item { ScanEmptyView(requireLocation) }
                } else {
                    items(state, onClick, row)
                }
            }
            is ScanningState.Error -> item { ErrorSection() }
        }.exhaustive

        item { Spacer(modifier = Modifier.size(16.dp)) }
    }
}

private fun LazyListScope.items(
    devices: ScanningState.DevicesDiscovered,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
    deviceView: @Composable (DiscoveredBluetoothDevice) -> Unit,
) {
    val bondedDevices = devices.bonded
    val discoveredDevices = devices.notBonded

    if (bondedDevices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.bonded_devices),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
        items(bondedDevices) { device ->
            ClickableDeviceItem(device, onClick, deviceView)
        }
    }

    if (discoveredDevices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.discovered_devices),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }

        items(discoveredDevices) { device ->
            ClickableDeviceItem(device, onClick, deviceView)
        }
    }
}

@Composable
private fun ClickableDeviceItem(
    device: DiscoveredBluetoothDevice,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
    deviceView: @Composable (DiscoveredBluetoothDevice) -> Unit,
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .clickable { onClick(device) }
        .padding(horizontal = 8.dp, vertical = 8.dp)) {
        deviceView(device)
    }
}

@Composable
private fun ErrorSection() {
    Text(
        text = stringResource(id = R.string.scan_failed),
        color = MaterialTheme.colorScheme.error
    )
}

/*
@Composable
private fun ProvisioningSection(data: ProvisioningData) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.version, data.version),
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.size(8.dp))

        if (data.isConnected) {
            RssiIcon(rssi = data.rssi)
        } else if (data.isProvisioned) {
            Icon(
                painter = painterResource(id = R.drawable.ic_wifi_error),
                contentDescription = null,
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_wifi),
                contentDescription = null,
            )
        }
    }
}
*/