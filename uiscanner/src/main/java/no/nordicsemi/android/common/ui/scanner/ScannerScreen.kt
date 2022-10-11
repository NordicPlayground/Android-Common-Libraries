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
package no.nordicsemi.android.common.ui.scanner

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import no.nordicsemi.android.common.permission.RequireBluetooth
import no.nordicsemi.android.common.permission.RequireLocation
import no.nordicsemi.android.common.ui.scanner.main.DevicesListView
import no.nordicsemi.android.common.ui.scanner.main.FilterView
import no.nordicsemi.android.common.ui.scanner.main.ScannerAppBar
import no.nordicsemi.android.common.ui.scanner.main.viewmodel.ScannerViewModel

@Composable
fun ScannerScreen(
    title: String = stringResource(id = R.string.scanner_screen),
    uuid: ParcelUuid?,
    cancellable: Boolean = true,
    onResult: (ScannerScreenResult) -> Unit,
) {
    var isScanning by rememberSaveable { mutableStateOf(false) }

    Column {
        if (cancellable) {
            ScannerAppBar(title, isScanning) { onResult(ScanningCancelled) }
        } else {
            ScannerAppBar(title, isScanning)
        }
        RequireBluetooth(
            onChanged = { isScanning = it }
        ) {
            RequireLocation(
                onChanged = { isScanning = it }
            ) { isLocationRequiredAndDisabled ->
                val viewModel = hiltViewModel<ScannerViewModel>()
                    .apply { setFilterUuid(uuid) }

                val state by viewModel.state.collectAsState()
                val config by viewModel.filterConfig.collectAsState()

                FilterView(
                    config = config,
                    onChanged = { viewModel.setFilter(it) }
                )

                val swipeRefreshState = rememberSwipeRefreshState(false)

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.refresh() },
                ) {
                    DevicesListView(
                        isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onResult(DeviceSelected(it)) }
                    )
                }
            }
        }
    }
}
