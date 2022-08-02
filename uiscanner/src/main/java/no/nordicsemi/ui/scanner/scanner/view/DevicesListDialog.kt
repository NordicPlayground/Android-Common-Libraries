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

package no.nordicsemi.ui.scanner.scanner.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.scanner.repository.ErrorResult
import no.nordicsemi.ui.scanner.scanner.repository.LoadingResult
import no.nordicsemi.ui.scanner.scanner.repository.ScanningState
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import no.nordicsemi.ui.scanner.ui.exhaustive

@Composable
internal fun DevicesListView(
    requireLocation: Boolean,
    devices: ScanningState,
    deviceView: @Composable (DiscoveredBluetoothDevice) -> Unit,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        item { Spacer(modifier = Modifier.size(8.dp)) }

        if (devices.isEmpty()) {
            item { ScanEmptyView(requireLocation) }
        } else {
            when (devices.result) {
                is LoadingResult -> item { ScanEmptyView(requireLocation) }
                is SuccessResult -> items(devices, onClick, deviceView)
                is ErrorResult -> item { ErrorSection() }
            }.exhaustive
        }

        item { Spacer(modifier = Modifier.size(16.dp)) }
    }
}

private fun LazyListScope.items(
    devices: ScanningState,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
    deviceView: @Composable (DiscoveredBluetoothDevice) -> Unit,
) {
    val bondedDevices = devices.bonded
    val discoveredDevices = devices.discovered

    if (bondedDevices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.bonded_devices),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
        items(bondedDevices) { device ->
            ClickableDeviceItem(device = device, onClick = onClick, deviceView = deviceView)
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
            ClickableDeviceItem(device = device, onClick = onClick, deviceView = deviceView)
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
fun DeviceItem(
    device: DiscoveredBluetoothDevice,
    modifier: Modifier = Modifier,
    extras: @Composable (DiscoveredBluetoothDevice) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically)
    {
        Image(
            painter = painterResource(R.drawable.ic_bluetooth),
            contentDescription = "Content image",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                )
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            device.displayName()?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            } ?: Text(
                text = stringResource(id = R.string.device_no_name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(text = device.displayAddress(), style = MaterialTheme.typography.bodyMedium)
        }

        extras(device)
    }
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
@Composable
private fun ErrorSection() {
    Text(
        text = stringResource(id = R.string.scan_failed),
        color = MaterialTheme.colorScheme.error
    )
}
