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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.WifiFind
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.flowlayout.FlowRow
import no.nordicsemi.android.theme.CheckboxFallback
import no.nordicsemi.android.theme.view.RssiIcon
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.ProvisioningData
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.ErrorResult
import no.nordicsemi.ui.scanner.scanner.repository.LoadingResult
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import no.nordicsemi.ui.scanner.ui.FlowCanceled
import no.nordicsemi.ui.scanner.ui.ItemSelectedResult
import no.nordicsemi.ui.scanner.ui.StringListDialogConfig
import no.nordicsemi.ui.scanner.ui.exhaustive

@Composable
internal fun DevicesListDialog(requireLocation: Boolean, config: StringListDialogConfig) {
    Dialog(onDismissRequest = { config.onResult(FlowCanceled) }) {
        DevicesListView(requireLocation, config)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DevicesListView(requireLocation: Boolean, config: StringListDialogConfig) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {

        item { Spacer(modifier = Modifier.size(8.dp)) }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                config.filterItems.forEachIndexed { i, item ->
                    ElevatedFilterChip(
                        selected = item.isChecked,
                        onClick = { config.onFilterItemCheckChanged(i) },
                        label = { Text(text = item.text) },
                        leadingIcon = {
                            item.icon?.let {
                                Image(
                                    painter = painterResource(id = it),
                                    contentDescription = ""
                                )
                            }
                        },
                        selectedIcon = { Icon(Icons.Default.Done, contentDescription = "") }
                    )
                }
            }
        }

        if (config.result.size() == 0) {
            item { ScanEmptyView(requireLocation) }
        } else {
            when (config.result.devices) {
                is LoadingResult -> item { ScanEmptyView(requireLocation) }
                is SuccessResult -> DevicesSection(config.result, config)
                is ErrorResult -> item { ErrorSection() }
            }.exhaustive
        }

        item { Spacer(modifier = Modifier.size(16.dp)) }
    }
}

private fun LazyListScope.DevicesSection(
    items: AllDevices,
    config: StringListDialogConfig
) {
    if (items.bondedDevices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.bonded_devices),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(items.bondedDevices.size) { i ->
            DeviceItem(device = items.bondedDevices[i], config = config)
        }
    }

    val devices = items.discoveredDevices

    if (devices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.discovered_devices),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(devices.size) { i ->
            DeviceItem(device = devices[i], config = config)
        }
    }
}

@Composable
private fun DeviceItem(
    device: DiscoveredBluetoothDevice,
    config: StringListDialogConfig
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { config.onResult(ItemSelectedResult(device)) }
            .padding(8.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            config.leftIcon?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = "Content image",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val deviceName = device.displayName()
                if (deviceName != null) {
                    Text(
                        text = deviceName,
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.device_no_name),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Text(text = device.displayAddress(), style = MaterialTheme.typography.bodyMedium)
            }

            device.provisioningData()?.let {
                ProvisioningSection(it)
            }
        }
    }
}

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

@Composable
private fun ErrorSection() {
    Text(
        text = stringResource(id = R.string.scan_failed),
        color = MaterialTheme.colorScheme.error
    )
}
