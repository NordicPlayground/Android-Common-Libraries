/*
 * Copyright (c) 2025, Nordic Semiconductor
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

package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.permissions.ble.RequireBluetooth
import no.nordicsemi.android.common.permissions.ble.RequireLocation
import no.nordicsemi.android.common.scanner.spec.ServiceUuids
import no.nordicsemi.android.common.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.ui.view.CircularIcon
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerView() {
    val scannerViewModel = hiltViewModel<ScannerViewModel>()
    val uiState by scannerViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ScannerAppBar(
                { Text(text = "Scanner") },
                showProgress = true,
                onNavigationButtonClick = {
//                    TODO: Handle back button click
                }
            )
        }
    ) { paddingValues ->
        val pullToRefreshState = rememberPullToRefreshState()
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RequireBluetooth {
                RequireLocation { isLocationRequiredAndDisabled ->
                    // Both Bluetooth and Location permissions are granted.
                    // If the permission is not granted then the scanning will not start.
                    // So to start scanning we need to check if the location permission is granted.
                    LaunchedEffect(isLocationRequiredAndDisabled) {
                        scannerViewModel.startScanning()
                    }
                    Column(modifier = Modifier.fillMaxSize()) {
                        PullToRefreshBox(
                            isRefreshing = uiState.scanningState is ScanningState.Loading,
                            onRefresh = {
                                scannerViewModel.refreshScanning()
                                scope.launch {
                                    pullToRefreshState.animateToHidden()
                                }
                            },
                            state = pullToRefreshState,
                            content = {
                                DeviceListView(
                                    isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                                    scanningState = uiState.scanningState,
                                    modifier = Modifier.fillMaxSize(),
                                    onClick = { },
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScannerViewPreview() {
    ScannerView()
}

@Composable
internal fun DeviceListView(
    isLocationRequiredAndDisabled: Boolean,
    scanningState: ScanningState,
    modifier: Modifier = Modifier,
    onClick: (ScanResult) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        when (scanningState) {
            is ScanningState.Loading -> item {
                ScanEmptyView(
                    locationRequiredAndDisabled = isLocationRequiredAndDisabled,
                )
            }

            is ScanningState.Error -> item { ScanErrorView(scanningState.error) }
            is ScanningState.DevicesDiscovered -> {
                if (scanningState.result.isEmpty()) {
                    item { ScanEmptyView(isLocationRequiredAndDisabled) }
                } else {
                    DeviceListItems(scanningState.result, onClick)
                }
            }
        }
    }

}


@Suppress("FunctionName")
internal fun LazyListScope.DeviceListItems(
    devices: List<ScanResult>,
    onClick: (ScanResult) -> Unit,
) {
    items(devices.size) { index ->
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { onClick(devices[index]) }
                .padding(8.dp)
        ) {
            DeviceListItem(
                device = devices[index],
                onClick = {}
            )
        }
    }
}

@Composable
private fun DeviceListItem(
    peripheralIcon: ImageVector = Icons.Default.Bluetooth,
    device: ScanResult,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        CircularIcon(peripheralIcon)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = device.peripheral.name ?: "Unknown device",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = device.peripheral.address,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
private fun ScanResultInfoRow(
    peripheralIcon: ImageVector = Icons.Default.Bluetooth,
    deviceName: String? = null,
    deviceAddress: String = "00:00:00:00:00:00",
    serviceUuids: List<Uuid> = emptyList(),
    rssi: Int = -100,
    onClick: () -> Unit = {},
) {
    Row {
        CircularIcon(peripheralIcon)

        Column {
            Text(
                text = deviceName
                    ?: "N/A", // TODO: If name is null, then show N/A with service type of the device.
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = deviceAddress,
                style = MaterialTheme.typography.bodyMedium
            )

            if (serviceUuids.isNotEmpty()) {
                serviceUuids.forEach {
                    ServiceUuids.getServiceInfo(it)?.let { serviceInfo ->
                        Row {
                            Text(
                                text = serviceInfo.service,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Icon(
                                imageVector = serviceInfo.icon,
                                contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }

    }

}


