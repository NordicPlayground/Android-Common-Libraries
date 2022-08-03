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

package no.nordicsemi.android.common.ui.scanner.navigation.view

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager.MODE_CHANGED_ACTION
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.android.common.ui.scanner.navigation.viewmodel.*
import no.nordicsemi.android.common.ui.scanner.view.error.*
import no.nordicsemi.android.common.ui.scanner.view.ScannerScreen
import no.nordicsemi.android.common.ui.scanner.view.event.Event

@Composable
fun FindDeviceScreen(
    deviceView: @Composable (DiscoveredBluetoothDevice) -> Unit,
) {
    val viewModel = hiltViewModel<ScannerNavigationViewModel>()

    val destination = viewModel.destination.collectAsState().value

    val context = LocalContext.current
    val activity = context as Activity
    BackHandler { viewModel.onEvent(Event.NavigateUp) }

    val onEvent = { event: Event ->
        viewModel.onEvent(event)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (destination) {
            BluetoothDisabledDestination -> BluetoothDisabledView(onEvent)
            BluetoothNotAvailableDestination -> BluetoothNotAvailableView(onEvent)
            BluetoothPermissionRequiredDestination -> BluetoothPermissionRequiredView(
                viewModel.utils.isBluetoothScanPermissionDeniedForever(
                    activity
                ),
                onEvent
            )
            LocationPermissionRequiredDestination -> LocationPermissionRequiredView(
                viewModel.utils.isLocationPermissionDeniedForever(
                    activity
                ),
                onEvent
            )
            PeripheralDeviceRequiredDestination -> ScannerScreen(viewModel.filterId, onEvent, deviceView)
            else -> {
                // Not possible, no view
            }
        }
    }

    ReceiverEffect(IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)) {
        onEvent(Event.RefreshNavigation)
    }
    ReceiverEffect(IntentFilter(MODE_CHANGED_ACTION)) {
        onEvent(Event.RefreshNavigation)
    }
}

@Composable
private fun ReceiverEffect(
    intentFilter: IntentFilter,
    onBroadcastReceived: () -> Unit,
) {
    val context = LocalContext.current

    DisposableEffect(context) {
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onBroadcastReceived()
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}
