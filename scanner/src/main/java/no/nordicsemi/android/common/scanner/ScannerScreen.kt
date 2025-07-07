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

@file:Suppress("unused")

package no.nordicsemi.android.common.scanner

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.scanner.view.DeviceListItem
import no.nordicsemi.android.common.scanner.view.FilterDialog
import no.nordicsemi.android.common.scanner.view.ScannerAppBar
import no.nordicsemi.android.common.scanner.view.ScannerView
import no.nordicsemi.kotlin.ble.client.android.ScanResult

/**
 * A scanner screen with an AppBar and a list of devices.
 *
 * @param cancellable If true, the screen will have a navigation button to cancel scanning.
 * @param onResultSelected Callback invoked when a device is selected or scanning is cancelled.
 * @param modifier Modifier to be applied to the screen.
 * @param state The state of the scan filter. Use this to set a static filter and dynamic filters.
 * @param title Composable function to display the title of the App Bar.
 * @param deviceItem Composable function to display each device in the list.
 */
@Composable
fun ScannerScreen(
    cancellable: Boolean,
    onResultSelected: (ScannerScreenResult) -> Unit,
    modifier: Modifier = Modifier,
    state: ScanFilterState = rememberFilterState(),
    title: @Composable () -> Unit = { Text(stringResource(id = R.string.scanner_screen)) },
    deviceItem: @Composable (ScanResult) -> Unit = { scanResult ->
        DeviceListItem(scanResult)
    },
) {
    var isScanning by rememberSaveable { mutableStateOf(false) }
    var expandFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        if (cancellable) {
            ScannerAppBar(
                title = title,
                isScanning = isScanning,
                state = state,
                onFilterClicked = { expandFilterBottomSheet = true },
                onNavigationButtonClick = { onResultSelected(ScanningCancelled) }
            )
        } else {
            ScannerAppBar(
                title = title,
                isScanning = isScanning,
                state = state,
                onFilterClicked = { expandFilterBottomSheet = true },
            )
        }

        ScannerView(
            state = state,
            onScanningStateChanged = { isScanning = it },
            onScanResultSelected = { onResultSelected(DeviceSelected(it)) },
            deviceItem = deviceItem,
        )
    }

    if (expandFilterBottomSheet) {
        FilterDialog(
            state = state,
            onDismissRequest = { expandFilterBottomSheet = false },
        )
    }
}
