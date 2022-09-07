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

package no.nordicsemi.android.common.permission

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import no.nordicsemi.android.common.permission.bluetooth.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.bluetooth.BluetoothStateManager
import no.nordicsemi.android.common.permission.internet.InternetPermissionResult
import no.nordicsemi.android.common.permission.internet.InternetStateManager
import javax.inject.Inject

/**
 * Needed for injecting to @Composable functions.
 */
@HiltViewModel
class PermissionViewModel @Inject internal constructor(
    internetManager: InternetStateManager,
    private val bluetoothManager: BluetoothStateManager,
) : ViewModel() {

    val bluetoothPermission = bluetoothManager.bluetoothState()
        .stateIn(viewModelScope, SharingStarted.Lazily, BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE)

    val locationPermission = bluetoothManager.locationState()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val internetPermission = internetManager.networkState()
        .map {
            when (it) {
                true -> InternetPermissionResult.ALL_GOOD
                false -> InternetPermissionResult.INTERNET_DISABLED
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, InternetPermissionResult.INTERNET_DISABLED)

    fun refreshPermission() {
        bluetoothManager.refreshPermission()
    }

    fun markLocationPermissionRequested() {
        bluetoothManager.markLocationPermissionRequested()
    }

    fun markBluetoothPermissionRequested() {
        bluetoothManager.markBluetoothPermissionRequested()
    }

    fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
        return bluetoothManager.isBluetoothScanPermissionDeniedForever(activity)
    }

    fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
        return bluetoothManager.isLocationPermissionDeniedForever(activity)
    }
}
