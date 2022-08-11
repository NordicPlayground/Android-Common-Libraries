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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult
import no.nordicsemi.android.common.permission.manager.LocalDataProvider
import no.nordicsemi.android.common.permission.manager.PermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PermissionManagerImpl @Inject internal constructor(
    private val utils: PermissionUtils,
    private val dataProvider: LocalDataProvider,
) : PermissionManager {
    private val _bluetoothPermission = MutableStateFlow(BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED)
    override val bluetoothPermission = _bluetoothPermission.asStateFlow()

    private val _internetPermission = MutableStateFlow(InternetPermissionResult.INTERNET_DISABLED)
    override val internetPermission = _internetPermission.asStateFlow()

    init {
        checkBluetooth()
        checkInternet()
    }

    override val isLocationPermissionRequired = dataProvider.locationState

    override fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(activity)
    }

    override fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isLocationPermissionDeniedForever(activity)
    }

    override fun onDevicesDiscovered() {
        if (utils.isLocationPermissionRequired && !utils.isLocationEnabled) {
            dataProvider.isLocationPermissionRequired = false
        }
        refreshLocationState()
    }

    override fun refreshLocationState() {
        dataProvider.refreshLocationState()
    }

    override fun checkBluetooth() {
        _bluetoothPermission.value = when {
            !utils.isBluetoothAvailable -> BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE
            !utils.isLocationPermissionGranted -> BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED
            !utils.areNecessaryBluetoothPermissionsGranted -> BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED
            !utils.isBleEnabled -> BluetoothPermissionResult.BLUETOOTH_DISABLED
            else -> BluetoothPermissionResult.ALL_GOOD
        }
    }

    override fun checkInternet() {
        _internetPermission.value = if (utils.isInternetEnabled) {
            InternetPermissionResult.INTERNET_DISABLED
        } else {
            InternetPermissionResult.ALL_GOOD
        }
    }
}
