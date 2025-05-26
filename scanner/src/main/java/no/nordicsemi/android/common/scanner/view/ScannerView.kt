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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.permissions.ble.RequireBluetooth
import no.nordicsemi.android.common.permissions.ble.RequireLocation
import no.nordicsemi.android.common.scanner.data.OnReloadScanResults
import no.nordicsemi.android.common.scanner.data.UiClickEvent
import no.nordicsemi.android.common.scanner.spec.ServiceUuids
import no.nordicsemi.android.common.scanner.viewmodel.ScannerUiState
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.ui.view.CircularIcon
import no.nordicsemi.android.common.ui.view.RssiIcon
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerView(
    title: String = "Scanner",
    uiState: ScannerUiState,
    startScanning: () -> Unit,
    onEvent: (UiClickEvent) -> Unit,
    onScanResultSelected: (ScanResult) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScannerAppBar(
            title = { Text(text = title) },
            showProgress = uiState.isScanning,
            scanningState = uiState.scanningState,
            filterConfig = uiState.filterConfig,
            onFilterSelected = { onEvent(it) },
            onNavigationButtonClick = {
                // TODO: Handle back navigation.
            }
        )

        RequireBluetooth {
            RequireLocation { isLocationRequiredAndDisabled ->
                // Both Bluetooth and Location permissions are granted.
                // If the permission is not granted then the scanning will not start.
                // So to start scanning we need to check if the location permission is granted.
                LaunchedEffect(isLocationRequiredAndDisabled) {
                    startScanning()
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    PullToRefreshBox(
                        isRefreshing = uiState.scanningState is ScanningState.Loading,
                        onRefresh = {
                            onEvent(OnReloadScanResults)
                            scope.launch {
                                pullToRefreshState.animateToHidden()
                            }
                        },
                        state = pullToRefreshState,
                        content = {
                            DeviceListView(
                                isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                                uiState = uiState,
                                onClick = { onScanResultSelected(it) },
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun DeviceListView(
    isLocationRequiredAndDisabled: Boolean,
    uiState: ScannerUiState,
    onClick: (ScanResult) -> Unit,
) {
    LazyColumn {
        when (uiState.scanningState) {
            is ScanningState.Loading -> item {
                ScanEmptyView(
                    locationRequiredAndDisabled = isLocationRequiredAndDisabled,
                )
            }

            is ScanningState.Error -> item { ScanErrorView(uiState.scanningState.error) }
            is ScanningState.DevicesDiscovered -> {
                if (uiState.scanningState.result.isEmpty()) {
                    item { ScanEmptyView(isLocationRequiredAndDisabled) }
                } else {
                    if (uiState.isGroupByNameEnabled) {
                        item {
                            GroupByNameDeviceList(
                                devices = uiState.scanningState.result,
                                onClick = onClick,
                            )
                        }
                    } else {
                        DeviceListItems(uiState.scanningState.result, onClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupByNameDeviceList(
    devices: List<ScanResult>,
    peripheralIcon: ImageVector = Icons.Default.Bluetooth,
    onClick: (ScanResult) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val expandIcon = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp)
        ) {
            CircularIcon(peripheralIcon)
            Text(text = devices.first().peripheral.name ?: "Unknown Device")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = expandIcon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandIn(expandFrom = Alignment.TopCenter) + fadeIn(),
            exit = shrinkOut(shrinkTowards = Alignment.TopCenter) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                HorizontalDivider()
                VerticalBlueBar {
                    devices.forEach { scanResult ->
                        ScanResultInfoRow(
                            device = scanResult,
                            peripheralIcon = null, // No icon for grouped items.
                            onScanResultSelected = onClick
                        )
                        // Add a divider between items if not the last item
                        if (scanResult != devices.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
        HorizontalDivider()
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.DeviceListItems(
    devices: List<ScanResult>,
    onScanResultSelected: (ScanResult) -> Unit,
) {
    items(devices.size) { index ->
        Box(
            modifier = Modifier
                .clickable {
                    onScanResultSelected(devices[index])
                }
        ) {
            DeviceListItem(
                device = devices[index],
                onClick = onScanResultSelected
            )
            if (index < devices.size) {
                // Add a divider between items
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun DeviceListItem(
    peripheralIcon: ImageVector = Icons.Default.Bluetooth,
    device: ScanResult,
    onClick: (ScanResult) -> Unit
) {
    val manufacturerData = device.advertisingData.manufacturerData

    ScanResultInfoRow(
        device = device,
        peripheralIcon = peripheralIcon,
        onScanResultSelected = onClick,
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun ScanResultInfoRow(
    device: ScanResult,
    peripheralIcon: ImageVector? = Icons.Default.Bluetooth,
    onScanResultSelected: (ScanResult) -> Unit,
) {
    val serviceUuids = device.advertisingData.serviceUuids
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onScanResultSelected(device) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        peripheralIcon?.let { CircularIcon(peripheralIcon) }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = device.peripheral.name ?: "Unknown Device",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = device.peripheral.address,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                // Show RSSI if available
                RssiIcon(device.rssi)
            }
            if (serviceUuids.isNotEmpty()) {
                // Each service UUID is displayed with its icon in the row. instead of a column.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    serviceUuids.forEach {
                        ServiceUuids.getServiceInfo(it)?.let { serviceInfo ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = serviceInfo.service,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    imageVector = serviceInfo.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
