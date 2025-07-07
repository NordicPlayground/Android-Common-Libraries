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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.permissions.ble.RequireBluetooth
import no.nordicsemi.android.common.permissions.ble.RequireLocation
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.android.common.scanner.rememberFilterState
import no.nordicsemi.android.common.scanner.spec.ServiceUuids
import no.nordicsemi.android.common.scanner.data.ScannedPeripheral
import no.nordicsemi.android.common.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.scanner.viewmodel.UiState
import no.nordicsemi.android.common.ui.view.CircularIcon
import no.nordicsemi.android.common.ui.view.RssiIcon
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerView(
    onScanResultSelected: (ScanResult) -> Unit,
    state: ScanFilterState = rememberFilterState(),
    onScanningStateChanged: (Boolean) -> Unit = {},
    deviceItem: @Composable (ScanResult) -> Unit = { scanResult ->
        DeviceListItem(scanResult)
    }
) {
    val viewModel = hiltViewModel<ScannerViewModel>()
    viewModel.setFilterState(state)

    val isScanningChanged by rememberUpdatedState(onScanningStateChanged)

    RequireBluetooth(
        onChanged = { isEnabled ->
            if (!isEnabled) {
                isScanningChanged(false)
            }
        }
    ) {
        RequireLocation { isLocationRequiredAndDisabled ->
            LaunchedEffect(isLocationRequiredAndDisabled) {
                // This would start scanning on each orientation change,
                // but there is a flag set in the ViewModel to prevent that.
                // User needs to pull to refresh to start scanning again.
                viewModel.initiateScanning(timeout = 4.seconds)
            }

            val pullToRefreshState = rememberPullToRefreshState()
            val scope = rememberCoroutineScope()

            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = {
                    viewModel.reload()
                    scope.launch {
                        pullToRefreshState.animateToHidden()
                    }
                },
                state = pullToRefreshState,
            ) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                DisposableEffect(uiState.isScanning) {
                    isScanningChanged(uiState.isScanning)
                    onDispose {
                        isScanningChanged(false)
                    }
                }
                println("Recomposing scanner view content")

                ScannerContent(
                    isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                    uiState = uiState,
                    onClick = onScanResultSelected,
                    deviceItem = deviceItem
                )
            }
        }
    }
}

@Composable
internal fun ScannerContent(
    isLocationRequiredAndDisabled: Boolean,
    uiState: UiState,
    onClick: (ScanResult) -> Unit,
    deviceItem: @Composable (ScanResult) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.displayCutout
            .only(WindowInsetsSides.Horizontal)
            .union(WindowInsets(left = 8.dp, right = 8.dp, top = 8.dp, bottom = 8.dp))
            .asPaddingValues()
    ) {
        when (uiState.scanningState) {
            is ScanningState.Loading -> item {
                ScanEmptyView(
                    locationRequiredAndDisabled = isLocationRequiredAndDisabled,
                )
            }
            is ScanningState.Error -> item {
                ScanErrorView(
                    error = uiState.scanningState.error
                )
            }
            is ScanningState.DevicesDiscovered -> {
                if (uiState.scanningState.result.isEmpty()) {
                    item {
                        ScanEmptyView(
                            locationRequiredAndDisabled = isLocationRequiredAndDisabled
                        )
                    }
                } else {
                    DeviceListItems(
                        devices = uiState.scanningState.result,
                        onScanResultSelected = onClick,
                        deviceItem = deviceItem
                    )
                }
            }
        }
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.DeviceListItems(
    devices: List<ScannedPeripheral>,
    onScanResultSelected: (ScanResult) -> Unit,
    deviceItem: @Composable (ScanResult) -> Unit = { scanResult ->
        DeviceListItem(scanResult, peripheralIcon = Icons.Default.Bluetooth)
    },
) {
    items(devices) {device ->
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { onScanResultSelected(device.latestScanResult) }
        ) {
            deviceItem(device.latestScanResult)
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun DeviceListItem(
    result: ScanResult,
    peripheralIcon: ImageVector? = result.advertisingData.serviceUuids
        .firstNotNullOfOrNull { ServiceUuids.getServiceInfo(it)?.icon }
        ?: Icons.Default.Bluetooth,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                        text = result.peripheral.name ?: stringResource(R.string.no_name),
                        color = if (result.peripheral.name != null) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = result.peripheral.address,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                // Show RSSI if available
                RssiIcon(result.rssi)
            }
        }
    }
}
