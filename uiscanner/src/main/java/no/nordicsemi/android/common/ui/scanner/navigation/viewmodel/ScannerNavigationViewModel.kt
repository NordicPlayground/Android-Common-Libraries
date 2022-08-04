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

package no.nordicsemi.android.common.ui.scanner.navigation.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.common.navigation.*
import no.nordicsemi.android.common.ui.scanner.ScannerDestinationId
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice
import no.nordicsemi.android.common.ui.scanner.navigation.*
import no.nordicsemi.android.common.ui.scanner.util.Utils
import javax.inject.Inject

@HiltViewModel
internal class ScannerNavigationViewModel @Inject constructor(
    val utils: Utils,
    private val navigationManager: NavigationManager
) : ViewModel() {
    val args = navigationManager.getImmediateArgument(ScannerDestinationId) as UUIDArgument?
    val filterId = args?.value?.let { ParcelUuid(it) }
    val destination = MutableStateFlow(getNextScreenDestination())

    private var device: DiscoveredBluetoothDevice? = null

    fun onEvent(event: ScannerNavigationEvent) {
        when (event) {
            ScannerNavigationEvent.NavigateUp -> navigationManager.navigateUp(
                ScannerDestinationId, CancelDestinationResult(
                    ScannerDestinationId
                ))
            ScannerNavigationEvent.Refresh -> refreshNavigation()
            is ScannerNavigationEvent.DeviceSelected -> onDeviceSelected(event.device)
        }
    }

    private fun onDeviceSelected(device: DiscoveredBluetoothDevice) {
        this.device = device
        refreshNavigation()
    }

    private fun refreshNavigation() {
        val nextDestination = getNextScreenDestination()

        if (nextDestination == null) {
            navigationManager.navigateUp(
                ScannerDestinationId,
                SuccessDestinationResult(ScannerDestinationId, ParcelableArgument(device!!))
            )
        } else if (destination.value != nextDestination) {
            destination.value = nextDestination
        }
    }

    private fun getNextScreenDestination(): ScannerNavigationDestination? {
        return when {
            !utils.isBluetoothAvailable -> BluetoothNotAvailableDestination
            !utils.isLocationPermissionGranted -> LocationPermissionRequiredDestination
            !utils.areNecessaryBluetoothPermissionsGranted -> BluetoothPermissionRequiredDestination
            !utils.isBleEnabled -> BluetoothDisabledDestination
            device == null -> PeripheralDeviceRequiredDestination
            else -> null
        }
    }
}
