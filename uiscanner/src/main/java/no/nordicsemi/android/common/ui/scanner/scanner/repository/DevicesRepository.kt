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

package no.nordicsemi.android.common.ui.scanner.scanner.repository

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import no.nordicsemi.android.common.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.android.common.ui.scanner.LocalDataProvider
import no.nordicsemi.android.common.ui.scanner.Utils
import javax.inject.Inject

@ViewModelScoped
internal class DevicesRepository @Inject constructor(
    private val utils: Utils,
    private val dataProvider: LocalDataProvider,
    private val devicesDataStore: DevicesDataStore
) {
    fun getDevices(): Flow<ScanningState> =
        callbackFlow<DeviceResource<List<DiscoveredBluetoothDevice>>> {
            val scanCallback: ScanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    // This callback will be called only if the scan report delay is not set or is set to 0.

                    // If the packet has been obtained while Location was disabled, mark Location as not required
                    if (utils.isLocationPermissionRequired && !utils.isLocationEnabled) {
                        dataProvider.isLocationPermissionRequired = false
                    }
                    if (result.isConnectable) {
                        devicesDataStore.addNewDevice(result)

                        trySend(DeviceResource.createSuccess(devicesDataStore.devices))
                    }
                }

                override fun onBatchScanResults(results: List<ScanResult>) {
                    // This callback will be called only if the report delay set above is greater then 0.

                    // If the packet has been obtained while Location was disabled, mark Location as not required
                    if (utils.isLocationPermissionRequired && !utils.isLocationEnabled) {
                        dataProvider.isLocationPermissionRequired = false
                    }
                    val newResults = results.filter { it.isConnectable }
                    newResults.forEach {
                        devicesDataStore.addNewDevice(it)
                    }
                    if (newResults.isNotEmpty()) {
                        trySend(DeviceResource.createSuccess(devicesDataStore.devices))
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    trySend(DeviceResource.createError(errorCode))
                }
            }

            trySend(DeviceResource.createLoading())

            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setLegacy(false)
                .setReportDelay(500)
                .setUseHardwareBatchingIfSupported(false)
                .build()
            val scanner = BluetoothLeScannerCompat.getScanner()
            scanner.startScan(null, settings, scanCallback)

            awaitClose {
                scanner.stopScan(scanCallback)
            }
        }.map { ScanningState(it) }

    fun clear() {
        devicesDataStore.clear()
    }
}
